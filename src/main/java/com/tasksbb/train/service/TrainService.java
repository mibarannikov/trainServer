package com.tasksbb.train.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksbb.train.dto.*;
import com.tasksbb.train.entity.*;
import com.tasksbb.train.entity.enums.EStatus;
import com.tasksbb.train.ex.ScheduleNotFoundException;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.ex.TrainNotFoundException;
import com.tasksbb.train.facade.SeatFacade;
import com.tasksbb.train.facade.TrainFacade;
import com.tasksbb.train.facade.TransferFacade;
import com.tasksbb.train.facade.WagonFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrainService {
    public static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    private static final Long MINIMUM_BOARDING_TIME = 120L;

    private final TrainEntityRepository trainEntityRepository;

    private final StationEntityRepository stationEntityRepository;

    private final PointOfScheduleRepository pointOfScheduleRepository;

    private final WagonEntityRepository wagonEntityRepository;


    @Transactional
    public TrainEntity addTrain(TrainDto trainDto) {
        TrainEntity addTrain = new TrainEntity();
        addTrain.setTrainNumber(trainDto.getTrainNumber());
        addTrain.setTrainSpeed(trainDto.getTrainSpeed());
        addTrain.setDepartureTime(trainDto.getDepartureTime());
        addTrain.setArrivalTimeEnd(trainDto.getArrivalTimeEnd());
        addTrain = trainEntityRepository.save(addTrain);
        for (WagonDto w : trainDto.getWagons()) {
            WagonEntity wagon = new WagonEntity();
            wagon.setWagonNumber(w.getWagonNumber());
            wagon.setType(w.getType());
            wagon.setNameWagon(w.getName());
            wagon.setSumSeats(w.getSumSeats());
            wagon.setTrainEntity(addTrain);
            addTrain.getWagonEntities().add(wagon);
        }
        for (int i = 1; i <= trainDto.getSumSeats(); i++) {
            SeatEntity s = new SeatEntity();
            s.setSeatNumber((long) i);
            s.setTrainEntity(addTrain);
            s.setTickets(new ArrayList<>());
            addTrain.getSeatEntities().add(s);
        }
        for (int i = 0; i < trainDto.getPointsOfSchedule().size(); i++) {
            PointOfScheduleEntity p = new PointOfScheduleEntity();
            p.setArrivalTime(trainDto.getPointsOfSchedule().get(i).getArrivalTime());
            p.setDepartureTime(trainDto.getPointsOfSchedule().get(i).getDepartureTime());
            p.setStationEntity(stationEntityRepository.findByNameStation(trainDto.getPointsOfSchedule().get(i).getNameStation())
                    .orElseThrow(() -> new StationNotFoundException("Station not found")));
            LOG.info("Station name" + trainDto.getPointsOfSchedule().get(i).getNameStation());
            p.setTrainEntity(addTrain);
            addTrain.getPointOfSchedules().add(p);
        }

        return trainEntityRepository.save(addTrain);
    }

    public List<TrainDto> findAllStartEndTimePeriod(String startStationName, String endStationName, LocalDateTime startTimePeriod, LocalDateTime endTimePeriod) {
        if (startTimePeriod.isBefore(LocalDateTime.now())) {
            startTimePeriod = LocalDateTime.now();
        }
        if (startTimePeriod.isAfter(endTimePeriod)) {
            endTimePeriod = endTimePeriod.plusMonths(1);
        }
        List<PointOfScheduleEntity> pointsStart = pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(startStationName, startTimePeriod, endTimePeriod);
        List<PointOfScheduleEntity> pointsEnd = pointOfScheduleRepository.findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(endStationName, LocalDateTime.now());
        List<TrainEntity> trains = new ArrayList<>();
        for (PointOfScheduleEntity p : pointsStart) {
            for (PointOfScheduleEntity pe : pointsEnd) {
                if ((p.getTrainEntity().getTrainNumber() == pe.getTrainEntity().getTrainNumber())
                        && (p.getDepartureTime().isBefore(pe.getArrivalTime()))
                        && (p.getDelayed() != EStatus.cancel) && (pe.getDelayed() != EStatus.cancel)) {
                    p.getTrainEntity().setAmountOfEmptySeats((long) emptySeats(p.getTrainEntity(), p, pe).size());
                    trains.add(p.getTrainEntity());
                }
            }
        }
        return trains.stream().map(TrainFacade::trainToDto).collect(Collectors.toList());
    }

    public List<TransferDto> findAllStartEndTimePeriodTransfer(String startStationName, String endStationName, LocalDateTime startTimePeriod, LocalDateTime endTimePeriod) {
        if (startTimePeriod.isBefore(LocalDateTime.now())) {
            startTimePeriod = LocalDateTime.now();
        }
        if (startTimePeriod.isAfter(endTimePeriod)) {
            endTimePeriod = endTimePeriod.plusMonths(1);
        }
        List<PointOfScheduleEntity> pointsStart = pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(startStationName, startTimePeriod, endTimePeriod);
        List<PointOfScheduleEntity> pointsEnd = pointOfScheduleRepository.findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(endStationName, LocalDateTime.now());

        List<PointOfScheduleEntity> allPointsFromStart = new ArrayList<>();
        for (PointOfScheduleEntity point : pointsStart) {
            allPointsFromStart.addAll(point.getTrainEntity().getPointOfSchedules().stream()
                    .filter(p -> p.getId() > point.getId())
                    .filter(p -> !p.getDelayed().equals(EStatus.cancel))
                    .collect(Collectors.toList()));
        }
        List<PointOfScheduleEntity> allPointsToEnd = new ArrayList<>();
        for (PointOfScheduleEntity point : pointsEnd) {
            allPointsToEnd.addAll(point.getTrainEntity().getPointOfSchedules().stream()
                    .filter(p -> p.getId() < point.getId())
                    .filter(p -> !p.getDelayed().equals(EStatus.cancel))
                    .collect(Collectors.toList()));
        }
        List<Transfer> transfers = new ArrayList<>();
        for (PointOfScheduleEntity pointSt : allPointsFromStart) {
            transfers.addAll(allPointsToEnd.stream()
                    .filter(p -> p.getStationEntity().equals(pointSt.getStationEntity()))
                    .filter(p -> p.getDepartureTime().isAfter(pointSt.getArrivalTime().plusHours(1)))
                    .map(p -> {
                        pointSt.getTrainEntity()
                                .setAmountOfEmptySeats((long) emptySeats(
                                        pointSt.getTrainEntity(),
                                        pointSt.getTrainEntity().getPointOfSchedules().stream()
                                                .filter(pt -> pt.getStationEntity().getNameStation().equals(startStationName))
                                                .findFirst().get(),
                                        pointSt).size());
                        p.getTrainEntity()
                                .setAmountOfEmptySeats((long) emptySeats(
                                        p.getTrainEntity(),
                                        p,
                                        p.getTrainEntity().getPointOfSchedules().stream()
                                                .filter(pt -> pt.getStationEntity().getNameStation().equals(endStationName))
                                                .findFirst().get()).size());
                        return new Transfer(pointSt.getTrainEntity(), p.getTrainEntity(), pointSt.getStationEntity());
                    })
                    .collect(Collectors.toList()));
        }

        return transfers.stream().map(TransferFacade::transferToTransferDto).collect(Collectors.toList());
    }

    public List<SeatEntity> emptySeats(TrainEntity train, PointOfScheduleEntity pointStart, PointOfScheduleEntity pointEnd) {

        LocalDateTime st = pointStart.getArrivalTime();
        LocalDateTime end = pointEnd.getDepartureTime();

        List<PointOfScheduleEntity> pointsOfFutureTicket = train.getPointOfSchedules().stream()
                .filter(point -> point.getDepartureTime().isAfter(st) && point.getArrivalTime().isBefore(end))
                .collect(Collectors.toList());
        boolean empty = true;
        List<SeatEntity> freeSeats = new ArrayList<>();
        for (SeatEntity seat : train.getSeatEntities()) {
            for (TicketEntity ticket : seat.getTickets()) {
                if (pointsOfFutureTicket.get(0).getDepartureTime().isBefore(ticket.getPointOfSchedules().get(ticket.getPointOfSchedules().size() - 1).getArrivalTime())
                        && pointsOfFutureTicket.get(pointsOfFutureTicket.size() - 1).getArrivalTime().isAfter(ticket.getPointOfSchedules().get(0).getDepartureTime())) {
                    empty = false;
                }
            }
            if (empty) {
                freeSeats.add(seat);
            }
            empty = true;
        }
        return freeSeats;
    }


    public List<SeatEntity> emptySeats(TrainEntity train, PointOfScheduleEntity pointStart, PointOfScheduleEntity pointEnd, Long wagonNumber) {
        int lastSeat = 0;
        for (int i = 0; i < wagonNumber - 1; i++) {
            lastSeat += train.getWagonEntities().get(i).getSumSeats();
        }
        LocalDateTime st = pointStart.getArrivalTime();
        LocalDateTime end = pointEnd.getDepartureTime();
        List<PointOfScheduleEntity> pointsOfFutureTicket = train.getPointOfSchedules().stream()
                .filter(point -> point.getDepartureTime().isAfter(st) && point.getArrivalTime().isBefore(end))
                .collect(Collectors.toList());
        boolean empty = true;
        List<SeatEntity> freeSeats = new ArrayList<>();
        for (SeatEntity seat : train.getSeatEntities()) {
            for (TicketEntity ticket : seat.getTickets()) {
                if (pointsOfFutureTicket.get(0).getDepartureTime().isBefore(ticket.getPointOfSchedules().get(ticket.getPointOfSchedules().size() - 1).getArrivalTime())
                        && pointsOfFutureTicket.get(pointsOfFutureTicket.size() - 1).getArrivalTime().isAfter(ticket.getPointOfSchedules().get(0).getDepartureTime())) {
                    empty = false;
                }
            }
            if (empty && (seat.getSeatNumber() > lastSeat) && (seat.getSeatNumber() <= lastSeat + train.getWagonEntities().get((int) (wagonNumber - 1)).getSumSeats())) {
                freeSeats.add(seat);
            }
            empty = true;
        }
        return freeSeats;
    }

    public List<SeatDto> getEmptySeats(Long trainNumber, String startStation, String endStation, Long wagonNumber) {
        TrainEntity train = trainEntityRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train with trainNumber " + trainNumber + "not found"));
        PointOfScheduleEntity pointStart = pointOfScheduleRepository
                .findByTrainEntityAndStationEntityNameStation(train, startStation)
                .orElseThrow(() -> new ScheduleNotFoundException("Point Of Schedule with station name " + startStation + " and train number" + train.getTrainNumber() + "not found"));
        PointOfScheduleEntity pointEnd = pointOfScheduleRepository
                .findByTrainEntityAndStationEntityNameStation(train, endStation)
                .orElseThrow(() -> new ScheduleNotFoundException("Point Of Schedule with station name " + startStation + " and train number" + train.getTrainNumber() + "not found"));
        List<SeatEntity> emptySeats = emptySeats(train, pointStart, pointEnd, wagonNumber);
        return emptySeats.stream().map(SeatFacade::seatToSeatDto).collect(Collectors.toList());
    }

    public PageDto getAllTrains(String param, int numberPage, int amount) {
        Pageable page = PageRequest.of(numberPage, amount);
        Page<TrainEntity> trainEntities = null;
        switch (param) {
            case "act": {
                trainEntities = trainEntityRepository.findByArrivalTimeEndAfter(LocalDateTime.now(), page);
                break;
            }
            case "past": {
                trainEntities = trainEntityRepository.findByArrivalTimeEndBeforeOrderByDepartureTimeDesc(LocalDateTime.now(), page);
                break;
            }
            case "all": {
                trainEntities = trainEntityRepository.findAllByOrderByDepartureTimeDesc(page);
                break;
            }
        }
        PageDto pageDto = new PageDto();
        pageDto.setContent(trainEntities.getContent().stream().map(TrainFacade::trainToDto).collect(Collectors.toList()));
        pageDto.setTotalElements(trainEntities.getTotalElements());
        return pageDto;  //trainEntities.stream().map(TrainFacade::trainToDto).collect(Collectors.toList());
    }

    public List<TrainDto> getTrainSchedule(String nameStation) {
        List<PointOfScheduleEntity> points;
        if (Objects.equals(nameStation, "all")) {
            points = pointOfScheduleRepository
                    .findAllByArrivalTimeAfterOrderByArrivalTime(LocalDateTime.now());
        } else {
            points = pointOfScheduleRepository
                    .findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(nameStation, LocalDateTime.now());
        }
        LOG.info("time now " + LocalDateTime.now());
        return points.stream()
                .map(point -> TrainFacade.trainToDto(point.getTrainEntity()))
                .collect(Collectors.toList());
    }

    public List<TrainDto> getAllActTrains() {
        return trainEntityRepository.findAll().stream()
                .map(TrainFacade::trainToDto)
                .collect(Collectors.toList());
    }

    public List<WagonDto> findAllWagon() {
        return wagonEntityRepository.findAll().stream().map(WagonFacade::wagonToWagonDto).collect(Collectors.toList());
    }

    public TrainDto findTrain(Long trainNumber) {
        return TrainFacade.trainToDto(trainEntityRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new TrainNotFoundException("Train with trainNumber " + trainNumber + "not found")));
    }

    @Transactional
    public TrainDto rollBackTrain(TrainDto trainDto) {
        for (PointOfScheduleDto point : trainDto.getPointsOfSchedule()) {
            PointOfScheduleEntity p = pointOfScheduleRepository.findById(point.getId())
                    .orElseThrow(() -> new ScheduleNotFoundException("Point with id " + point.getId() + "not found"));
            p.setDelayed(point.getDelayed());
            p.setDepartureTime(point.getDepartureTime());
            p.setArrivalTime(point.getArrivalTime());
            pointOfScheduleRepository.save(p);
        }
        return TrainFacade.trainToDto(trainEntityRepository.findByTrainNumber(trainDto.getTrainNumber()).get());
    }

    @Transactional
    public TrainDto updateTrain(TrainDto trainDto) {
        TrainEntity train = trainEntityRepository.findByTrainNumber(trainDto.getTrainNumber())
                .orElseThrow(() -> new TrainNotFoundException("Train with trainNumber " + trainDto.getTrainNumber() + " not found"));


        for (int i = 0; i < train.getPointOfSchedules().size(); i++) {
            if (train.getPointOfSchedules().get(i).getDelayed() != trainDto.getPointsOfSchedule().get(i).getDelayed()) {
                train.getPointOfSchedules().get(i).setDelayed(trainDto.getPointsOfSchedule().get(i).getDelayed());
            }
            if (train.getPointOfSchedules().get(i).getDepartureTimeInit().isBefore(trainDto.getPointsOfSchedule().get(i).getDepartureTime())) {
                if (i < train.getPointOfSchedules().size() - 1) {
                    trainDto.getPointsOfSchedule().get(i + 1).setArrivalTime(trainDto.getPointsOfSchedule().get(i + 1).getArrivalTime()
                            .plusSeconds(trainDto.getPointsOfSchedule().get(i).getDepartureTime().toEpochSecond(ZoneOffset.of("+0"))
                                    - train.getPointOfSchedules().get(i).getDepartureTime().toEpochSecond(ZoneOffset.of("+0"))));
                }

                train.getPointOfSchedules().get(i).setDepartureTime(trainDto.getPointsOfSchedule().get(i).getDepartureTime());

            }
            Long delta = trainDto.getPointsOfSchedule().get(i).getArrivalTime().toEpochSecond(ZoneOffset.of("+0"))
                    - train.getPointOfSchedules().get(i).getArrivalTimeInit().toEpochSecond(ZoneOffset.of("+0"));
            for (int j = i; j < train.getPointOfSchedules().size(); j++) {
                if (delta > 0) {
                    // train.getPointOfSchedules().get(j).setDelayed(EStatus.running_with_errors);
                    train.getPointOfSchedules().get(j).setArrivalTime(train.getPointOfSchedules().get(j).getArrivalTimeInit().plusSeconds(delta));
                    if ((train.getPointOfSchedules().get(j).getDepartureTime().toEpochSecond(ZoneOffset.of("+0"))
                            - train.getPointOfSchedules().get(j).getArrivalTime().toEpochSecond(ZoneOffset.of("+0"))) < MINIMUM_BOARDING_TIME) {
                        delta = train.getPointOfSchedules().get(j).getArrivalTime().toEpochSecond(ZoneOffset.of("+0")) + MINIMUM_BOARDING_TIME
                                - train.getPointOfSchedules().get(j).getDepartureTime().toEpochSecond(ZoneOffset.of("+0"));
                        train.getPointOfSchedules().get(j).setDepartureTime(train.getPointOfSchedules().get(j).getArrivalTime().plusSeconds(MINIMUM_BOARDING_TIME));

                    } else {
                        delta = 0L;
                        //   train.getPointOfSchedules().get(j).setDelayed(EStatus.schedule);
                    }
                }
            }
            train.getPointOfSchedules().forEach(p -> {
                if (p.getArrivalTime().isAfter(p.getArrivalTimeInit())) {
                    p.setDelayed(EStatus.running_with_errors);
                }
            });

            if (trainDto.getPointsOfSchedule().get(i).getDelayed() == EStatus.cancel) {
                train.getPointOfSchedules().stream().skip(i).forEach(point -> point.setDelayed(EStatus.cancel));
                break;
            }

        }
        train = trainEntityRepository.save(train);

        return TrainFacade.trainToDto(train);
    }
}

