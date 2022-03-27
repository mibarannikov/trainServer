package com.tasksbb.train.service;

import com.tasksbb.train.dto.SeatDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.dto.WagonDto;
import com.tasksbb.train.entity.*;
import com.tasksbb.train.ex.ScheduleNotFoundException;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.ex.TrainNotFoundException;
import com.tasksbb.train.facade.SeatFacade;
import com.tasksbb.train.facade.TrainFacade;
import com.tasksbb.train.facade.WagonFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrainService {
    public static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

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
        List<PointOfScheduleEntity> pointsStart = pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(startStationName, startTimePeriod, endTimePeriod);
        List<PointOfScheduleEntity> pointsEnd = pointOfScheduleRepository.findAllByStationEntityNameStationOrderByArrivalTimeAsc(endStationName);
        List<TrainEntity> trains = new ArrayList<>();
        for (PointOfScheduleEntity p : pointsStart) {
            for (PointOfScheduleEntity pe : pointsEnd) {
                if ((p.getTrainEntity().getTrainNumber() == pe.getTrainEntity().getTrainNumber())
                        && (p.getDepartureTime().isBefore(pe.getArrivalTime()))
                        && (p.getDelayed() != 3) && (pe.getDelayed() != 3)) {
                    p.getTrainEntity().setAmountOfEmptySeats((long) emptySeats(p.getTrainEntity(), p, pe).size());
                    trains.add(p.getTrainEntity());
                }
            }
        }
        return trains.stream().map(TrainFacade::trainToDto).collect(Collectors.toList());
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

    public List<TrainDto> getAllTrains(String param) {
        List<TrainEntity> trainEntities = new ArrayList<>();
        switch (param) {
            case "act": {
                trainEntities = trainEntityRepository.findByArrivalTimeEndAfter(LocalDateTime.now());
                break;
            }
            case "past": {
                trainEntities = trainEntityRepository.findByArrivalTimeEndBeforeOrderByDepartureTimeDesc(LocalDateTime.now());
                break;
            }
            case "all": {
                trainEntities = trainEntityRepository.findAllByOrderByDepartureTimeDesc();
                break;
            }
        }

        return trainEntities.stream().map(TrainFacade::trainToDto).collect(Collectors.toList());
    }

    public List<TrainDto> getTrainSchedule(String nameStation) {
        List<PointOfScheduleEntity> points = pointOfScheduleRepository
                .findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(nameStation, LocalDateTime.now());

        LOG.info("time now " + LocalDateTime.now());
        return points.stream()
                .map(point -> TrainFacade.trainToDto(point.getTrainEntity()))
                .collect(Collectors.toList());
    }

    public List<TrainDto> getAllActTrains() {
        return trainEntityRepository.findByArrivalTimeEndAfter(LocalDateTime.now()).stream()
                .map(TrainFacade::trainToDto)
                .collect(Collectors.toList());
    }

    public List<WagonDto> findAllWagon() {
        return wagonEntityRepository.findAll().stream().map(WagonFacade::wagonToWagonDto).collect(Collectors.toList());
    }
}
