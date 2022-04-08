package com.tasksbb.train.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainServiceTest {

    private TrainService trainService;

    private TrainEntityRepository trainEntityRepository;

    private StationEntityRepository stationEntityRepository;

    private PointOfScheduleRepository pointOfScheduleRepository;

    private WagonEntityRepository wagonEntityRepository;

    private  ObjectMapper mapper = new ObjectMapper();
    @Before
    void init() {
        trainEntityRepository = Mockito.mock(TrainEntityRepository.class);
        stationEntityRepository = Mockito.mock(StationEntityRepository.class);
        pointOfScheduleRepository = Mockito.mock(PointOfScheduleRepository.class);
        wagonEntityRepository = Mockito.mock(WagonEntityRepository.class);

        trainService = new TrainService(trainEntityRepository, stationEntityRepository, pointOfScheduleRepository, wagonEntityRepository);
    }

    private TrainDto createTrainDto() throws IOException {
        File f =  new File( "C:\\Users\\mibar\\OneDrive\\Рабочий стол\\to git\\trainServer\\src\\test\\resources\\train_dto.json");//Paths.get("train_dto.json").toFile();

        TrainDto trainDto = mapper.readValue(f, TrainDto.class);
        return trainDto;
    }

    @Test
    void addTrain() throws IOException {
        trainService.addTrain(createTrainDto());

        assertTrue(true);

    }

    @Test
    void findAllStartEndTimePeriod() {
    }

    @Test
    void findAllStartEndTimePeriodTransfer() {
    }

    @Test
    void emptySeats() {
    }

    @Test
    void testEmptySeats() {
    }

    @Test
    void getEmptySeats() {
    }

    @Test
    void getAllTrains() {
    }

    @Test
    void getTrainSchedule() {
    }

    @Test
    void getAllActTrains() {
    }

    @Test
    void findAllWagon() {
    }

    @Test
    void findTrain() {
    }

    @Test
    void rollBackTrain() {
    }

    @Test
    void updateTrain() {
    }
}
