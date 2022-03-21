package com.tasksbb.train;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class TrainApplicationTests {

    public final StationEntityRepository stationEntityRepository;

    public final  StationFacade stationFacade;

    public final  TrainEntityRepository trainEntityRepository;

    public final  PointOfScheduleRepository pointOfScheduleRepository;

    public TrainApplicationTests(StationEntityRepository stationEntityRepository, StationFacade stationFacade, TrainEntityRepository trainEntityRepository, PointOfScheduleRepository pointOfScheduleRepository) {
        this.stationEntityRepository = stationEntityRepository;
        this.stationFacade = stationFacade;
        this.trainEntityRepository = trainEntityRepository;
        this.pointOfScheduleRepository = pointOfScheduleRepository;
    }

    @Test
    void contextLoads() {
        TrainEntity train = new TrainEntity();
        train.setTrainNumber(100L);
        train.setDepartureTime(LocalDateTime.now());
        train.setTrainSpeed(40.0);
        SeatEntity seat = new SeatEntity();
        seat.setTickets(Collections.emptyList());
        seat.setSeatNumber(10L);
        seat.setTrainEntity(train);
        PointOfScheduleEntity point = new PointOfScheduleEntity();
        point.setStationEntity(stationEntityRepository.findByNameStation("f").get());
        point.setArrivalTime(LocalDateTime.now());
        point.setTrainEntity(train);
        train.getPointOfSchedules().add(point);
        train.getSeatEntities().add(seat);
        trainEntityRepository.save(train);


    }

    @Test
    void test2() {

        System.out.println(trainEntityRepository.findByTrainNumber(1L).get());
        System.out.println(pointOfScheduleRepository.findByTrainEntity(trainEntityRepository.findByTrainNumber(1L).get()));
        System.out.println(pointOfScheduleRepository.findAll());
    }

    @Test
    void test3() {
         StationEntity station = stationEntityRepository.findByNameStation("b").get();
        System.out.println(station);
        System.out.println(stationFacade.stationToStationDto(station));
    }

    @Test
    @Transactional
    void test4() {
        List<PointOfScheduleEntity> point = pointOfScheduleRepository.findPointOfScheduleEntityByTrainEntityId(11L);
        PointOfScheduleEntity point1 = pointOfScheduleRepository.findById(10L).get();
        System.out.println(point);
    }

}
