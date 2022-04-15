package com.tasksbb.train.service;

import com.tasksbb.train.dto.PointOfScheduleDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.*;
import com.tasksbb.train.ex.*;
import com.tasksbb.train.facade.TicketFacade;
import com.tasksbb.train.repository.*;
import com.tasksbb.train.service.constants.PriceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TicketService {

    private final SeatEntityRepository seatEntityRepository;

    private final TicketEntityRepository ticketEntityRepository;

    private final PointOfScheduleRepository pointOfScheduleRepository;

    private final PassengerEntityRepository passengerEntityRepository;

    private final TrainEntityRepository trainEntityRepository;

    private final StationService stationService;

    /**
     *
     * @param ticketDto
     * @param user
     * @return
     */
    @Transactional
    public TicketEntity buyTicket(TicketDto ticketDto, User user) {
        if (!timeValidationTicket(ticketDto)) {
            throw  new StationIsOblivionException("Посадка окончена");
        }
        TicketEntity newTicket = new TicketEntity();
        TrainEntity train = trainEntityRepository.findByTrainNumber(ticketDto.getNumberTrainOwner())
                .orElseThrow(() -> new TrainNotFoundException("Not found train with number " + ticketDto.getNumberTrainOwner()));
        Long amountSeats = 0L;
        for (WagonEntity w : train.getWagonEntities()) {
            if (w.getWagonNumber() == ticketDto.getWagonNumber()) {
                break;
            }
            amountSeats += w.getSumSeats();
        }
        amountSeats += ticketDto.getSeatNumber();

        SeatEntity seat = seatEntityRepository
                .findByTrainEntityTrainNumberAndSeatNumber(ticketDto.getNumberTrainOwner(), amountSeats)
                .orElseThrow(() -> new SeatNotFoundException("seat not found"));

        for (PointOfScheduleDto name : ticketDto.getNameStations()) {
            newTicket.getPointOfSchedules()
                    .add(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(seat.getTrainEntity(), name.getNameStation())
                            .orElseThrow(() ->
                                    new ScheduleNotFoundException(
                                            "Not found point of schedule for train number"
                                                    + seat.getTrainEntity().getTrainNumber()
                                                    + " and station "
                                                    + name.getNameStation()
                                    )));
        }
        Optional<PassengerEntity> passenger = passengerEntityRepository
                .findByFirstnameAndLastnameAndDateOfBirth(
                        ticketDto.getFirstnamePassenger(),
                        ticketDto.getLastnamePassenger(),
                        ticketDto.getDateOfBirth());
        if (passenger.isEmpty()) {
            PassengerEntity pass = new PassengerEntity();
            pass.setFirstname(ticketDto.getFirstnamePassenger());
            pass.setLastname(ticketDto.getLastnamePassenger());
            pass.setDateOfBirth(ticketDto.getDateOfBirth());
            newTicket.setPassengerEntity(pass);
        } else {
            if (passengerIsPresent(seat.getTrainEntity(), passenger.get(), newTicket.getPointOfSchedules())) {
                throw new PassengerInTrainException("Пассажир уже зарегистрироан");
            }
            newTicket.setPassengerEntity(passenger.get());
        }
        newTicket.setUser(user);
        newTicket.setSeatEntity(seat);
        newTicket = ticketEntityRepository.save(newTicket);
        log.info("Bought a ticket whit id {} , time {} ",newTicket.getId(), LocalDateTime.now());
        return newTicket;
    }



    private Boolean passengerIsPresent(TrainEntity train, PassengerEntity passenger, List<PointOfScheduleEntity> points) {
        List<TicketEntity> tickets = ticketEntityRepository.findBySeatEntity_TrainEntity_TrainNumber(train.getTrainNumber());
        for (TicketEntity tk : tickets) {
            if (tk.getPassengerEntity().equals(passenger)
                    && points.get(0).getDepartureTime().isBefore(tk.getPointOfSchedules().get(tk.getPointOfSchedules().size() - 1).getArrivalTime())
                    && points.get(points.size() - 1).getArrivalTime().isAfter(tk.getPointOfSchedules().get(0).getDepartureTime())) {
                return true;
            }
        }
        return false;
    }




    /**
     * @param user
     * @param param
     * @return
     */
    public List<TicketDto> getAllUserTickets(User user, String param) {
        List<TicketEntity> tickets = ticketEntityRepository.findAllByUser(user);
        if (Objects.equals(param, "act")) {
            tickets = tickets.stream()
                    .filter(tk -> tk.getPointOfSchedules()
                            .get(0)
                            .getArrivalTime()
                            .isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        return tickets.stream().map(TicketFacade::ticketToTicketDto).collect(Collectors.toList());
    }

    private boolean timeValidationTicket(TicketDto ticketDto) {
        return ticketDto.getNameStations().get(0).getDepartureTime().isAfter(LocalDateTime.now().plusMinutes(10));
    }

    public List<TicketDto> AllTrainTickets(Long trainNumber) {
        List<TicketEntity> tickets = ticketEntityRepository.findBySeatEntity_TrainEntity_TrainNumber(trainNumber);
        return tickets.stream().map(TicketFacade::ticketToTicketDto).collect(Collectors.toList());
    }

    public List<TicketDto> ticketsOnTheTrainNow(Long trainNumber) {
        List<PointOfScheduleEntity> points = pointOfScheduleRepository
                .findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(trainNumber, LocalDateTime.now());
        if (points.isEmpty()) {
            return new ArrayList<TicketDto>();// todo exception
        }
        List<TicketEntity> tickets = ticketEntityRepository
                .findAllByPointOfSchedules(points.get(points.size() - 1));
        return tickets.stream()
                .filter(t -> t.getPointOfSchedules().get(t.getPointOfSchedules().size() - 1) != points.get(points.size() - 1))
                .map(TicketFacade::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    /**
     * @param trainNumber
     * @param wagonNumber
     * @param startStation
     * @param endStation
     * @return
     */
    public String priceCalculation(Long trainNumber, Long wagonNumber, String startStation, String endStation) {
        TrainEntity train = trainEntityRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train with trainNumber " + trainNumber + "not found"));
        double totalDistance = 0.0;
        for (int i = train.getPointOfSchedules().indexOf(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(train, startStation).get()) + 1;
             i <= train.getPointOfSchedules().indexOf(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(train, endStation).get()); i++) {
            totalDistance += stationService.distanceCalculation(train.getPointOfSchedules().get(i), train.getPointOfSchedules().get(i - 1));
        }
        double coeffTypeWagon = 0.0;
        switch (train.getWagonEntities().stream().filter(wagon -> Objects.equals(wagon.getWagonNumber(), wagonNumber)).findFirst().get().getType()) {
            case "coupe": {
                coeffTypeWagon = PriceConstants.COUPE_COEFFICIENT;
                break;
            }
            case "platzkarte": {
                coeffTypeWagon = PriceConstants.PLAZCARTE_COEFFICIENT;
                break;
            }
            default: {
                coeffTypeWagon = 1.0;
                break;
            }
        }
        double price = totalDistance * PriceConstants.PRICE_PER_METER * coeffTypeWagon * train.getTrainSpeed() * PriceConstants.SPEED_COEFFICIENT;
        return String.format(Locale.FRANCE, "%,.2f", price) + "RUB";
    }


}
