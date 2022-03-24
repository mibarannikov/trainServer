package com.tasksbb.train.service;

import com.tasksbb.train.dto.PointOfScheduleDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.*;
import com.tasksbb.train.ex.ScheduleNotFoundException;
import com.tasksbb.train.facade.TicketFacade;
import com.tasksbb.train.repository.PassengerEntityRepository;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.SeatEntityRepository;
import com.tasksbb.train.repository.TicketEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final SeatEntityRepository seatEntityRepository;

    private final TicketEntityRepository ticketEntityRepository;

    private final PointOfScheduleRepository pointOfScheduleRepository;

    private final PassengerEntityRepository passengerEntityRepository;

    private final PassengerService passengerService;

    @Transactional
    public TicketDto buyTicket(TicketDto ticketDto, User user) {
        if (!timeValidationTicket(ticketDto)) {
            ticketDto.getNameStations().get(0).setNameStation("station whose name is oblivion");//todo throw exception
            return ticketDto;
        }
        TicketEntity newTicket = new TicketEntity();
        SeatEntity seat = seatEntityRepository
                .findByTrainEntityTrainNumberAndSeatNumber(ticketDto.getNumberTrainOwner(), ticketDto.getSeatNumber());// todo orElseThrow()

        for (PointOfScheduleDto name : ticketDto.getNameStations()) {
            newTicket.getPointOfSchedules()
                    .add(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(seat.getTrainEntity(), name.getNameStation())
                            .orElseThrow(() -> new ScheduleNotFoundException("Not found point of schedule for train number" + seat.getTrainEntity().getTrainNumber() + " and station " + name.getNameStation())));// todo orElseThrow()
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
            if (passengerService.passengerIsPresent(seat.getTrainEntity(), passenger.get(), newTicket.getPointOfSchedules())) {
                ticketDto.setId(0L);//todo throw exception
                return ticketDto;
            }
            newTicket.setPassengerEntity(passenger.get());
        }
        newTicket.setUser(user);
        newTicket.setSeatEntity(seat);
        newTicket = ticketEntityRepository.save(newTicket);
        return TicketFacade.ticketToTicketDto(newTicket);
    }

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

    @Transactional
    public List<TicketDto> ticketsOnTheTrainNow(Long trainNumber) {
        List<PointOfScheduleEntity> points = pointOfScheduleRepository
                .findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(trainNumber, LocalDateTime.now());
        if (points.isEmpty()) {
            return new ArrayList<TicketDto>();// todo exception
        }
        List<TicketEntity> tickets = ticketEntityRepository
                .findAllByPointOfSchedules(points.get(points.size() - 1));
        // tickets =tickets.stream().filter(t -> t.getPointOfSchedules().get(t.getPointOfSchedules().size()-1)!=points.get(points.size()-1)).collect(Collectors.toList());

        //List<SeatEntity> seats = seatEntityRepository.findByTrainEntity(train);
        //for (SeatEntity st : seats) {
        //    for (TicketEntity tck : st.getTickets()) {
        //        if (registeredTicket(tck)) {
        //            tickets.add(TicketFacade.ticketToTicketDto(tck));
        //        }
        //    }
        //}
        return tickets.stream()
                .filter(t -> t.getPointOfSchedules().get(t.getPointOfSchedules().size() - 1) != points.get(points.size() - 1))
                .map(TicketFacade::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    // private boolean registeredTicket(TicketEntity ticket) {
    //     boolean before = false;
    //     boolean after = false;
    //     for (PointOfScheduleEntity point : ticket.getPointOfSchedules()) {
    //         if (point.getArrivalTime().isBefore(LocalDateTime.now())) {
    //             before = true;
    //         }
//            if (point.getArrivalTime().isAfter(LocalDateTime.now())) {
//                after = true;
//            }
//            if (after && before) {
//                return true;
//            }
//        }
//        return false;
//    }
}
