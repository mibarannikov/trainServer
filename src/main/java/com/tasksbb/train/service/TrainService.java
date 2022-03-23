package com.tasksbb.train.service;

import com.tasksbb.train.dto.SeatEntityDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.ex.ScheduleNotFoundException;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.ex.TrainNotFoundException;
import com.tasksbb.train.facade.SeatFacade;
import com.tasksbb.train.facade.TrainFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
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


    @Transactional
    public TrainEntity addTrain(TrainDto trainDto) {
        TrainEntity addTrain = new TrainEntity();
        addTrain.setTrainNumber(trainDto.getTrainNumber());
        addTrain.setTrainSpeed(trainDto.getTrainSpeed());
        addTrain.setDepartureTime(trainDto.getDepartureTime());
        addTrain.setArrivalTimeEnd(trainDto.getArrivalTimeEnd());
        try {
            addTrain = trainEntityRepository.save(addTrain);
        } catch (Exception ex){
            throw new TrainNotFoundException("train number "+trainDto.getTrainNumber().toString()+" already exists");
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
                    .orElseThrow(()-> new StationNotFoundException("Station not found")));
            LOG.info("Station name"+ trainDto.getPointsOfSchedule().get(i).getNameStation());
            p.setTrainEntity(addTrain);
            addTrain.getPointOfSchedules().add(p);
        }

        return trainEntityRepository.save(addTrain);
    }

    public List<TrainDto> findAllStartEndTimePeriod(String startStationName, String endStationName, LocalDateTime startTimePeriod, LocalDateTime endTimePeriod) {

        List<PointOfScheduleEntity> pointsStart = pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(startStationName, startTimePeriod, endTimePeriod);
        List<PointOfScheduleEntity> pointsEnd = pointOfScheduleRepository.findAllByStationEntityNameStationOrderByArrivalTimeAsc(endStationName);
        List<TrainEntity> trains = new ArrayList<>();
        for (PointOfScheduleEntity p : pointsStart) {
            for (PointOfScheduleEntity pe : pointsEnd) {
                if ((p.getTrainEntity().getTrainNumber() == pe.getTrainEntity().getTrainNumber()) && (p.getDepartureTime().isBefore(pe.getArrivalTime()))) {
                    p.getTrainEntity().setAmountOfEmptySeats((long) emptySeats(p.getTrainEntity(), p, pe).size());
                    trains.add(p.getTrainEntity());
                }
            }
        }

        return trains.stream().map(TrainFacade::trainToDto).collect(Collectors.toList());
    }

//    private void trainScript(){
//       List<TrainEntity> trains = trainEntityRepository.findAll();
//       trains.forEach(train -> {
//           train.setArrivalTimeEnd(train.getPointOfSchedules().get(train.getPointOfSchedules().size()-1).getArrivalTime());
//           LOG.info(train.getTrainNumber().toString()+" "+train.getArrivalTimeEnd().toString());
//           trainEntityRepository.save(train);
//       });
//    }

    public List<SeatEntity> emptySeats(TrainEntity train, PointOfScheduleEntity pointStart, PointOfScheduleEntity pointEnd) {
        List<PointOfScheduleEntity> pointOfTrain = pointOfScheduleRepository.findAllByTrainEntityOrderByArrivalTimeAsc(train);
        LocalDateTime st = pointStart.getArrivalTime().minusMinutes(1);
        LocalDateTime end = pointEnd.getArrivalTime().plusMinutes(1);
        List<PointOfScheduleEntity> pointsOfFutureTicket = pointOfTrain.stream().filter(point -> point.getArrivalTime().isAfter(st) && point.getArrivalTime().isBefore(end))
                .collect(Collectors.toList());
        pointsOfFutureTicket.remove(pointsOfFutureTicket.size() - 1);
        boolean empty = true;
        List<SeatEntity> freeSeats = new ArrayList<>();
        for (SeatEntity seat : train.getSeatEntities()) {
            for (TicketEntity ticket : seat.getTickets()) {
                for (PointOfScheduleEntity point : ticket.getPointOfSchedules()) {
                    for (PointOfScheduleEntity pointFT : pointsOfFutureTicket) {
                        if (pointFT == point) {
                            empty = false;
                        }
                    }
                }
            }
            if (empty) {
                freeSeats.add(seat);
            }
            empty = true;
        }
        return freeSeats;
    }

    public List<SeatEntityDto> getEmptySeats(Long trainNumber, String startStation, String endStation) {
        TrainEntity train = trainEntityRepository.findByTrainNumber(trainNumber)
                .orElseThrow(()->new TrainNotFoundException("Train with trainNumber "+ trainNumber + "not found"));
        PointOfScheduleEntity pointStart = pointOfScheduleRepository
                .findByTrainEntityAndStationEntityNameStation(train, startStation)
                .orElseThrow(()->new ScheduleNotFoundException("Point Of Schedule with station name "+startStation+" and train number"+ train.getTrainNumber()+"not found"));
        PointOfScheduleEntity pointEnd = pointOfScheduleRepository
                .findByTrainEntityAndStationEntityNameStation(train, endStation)
                .orElseThrow(()->new ScheduleNotFoundException("Point Of Schedule with station name "+startStation+" and train number"+ train.getTrainNumber()+"not found"));
        List<SeatEntity> emptySeats = emptySeats(train, pointStart, pointEnd);
        return emptySeats.stream().map(SeatFacade::seatToSeatDto).collect(Collectors.toList());
    }

    public List<TrainDto> getAllTrains() {
        List<TrainEntity> trainEntities = trainEntityRepository.findAllByOrderByDepartureTimeAsc();
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
}
