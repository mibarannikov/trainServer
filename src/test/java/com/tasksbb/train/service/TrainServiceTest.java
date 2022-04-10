package com.tasksbb.train.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.tasksbb.train.dto.SeatDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.dto.TransferDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TrainServiceTest {

    private TrainService trainService;

    private TrainEntityRepository trainEntityRepository;

    private StationEntityRepository stationEntityRepository;

    private PointOfScheduleRepository pointOfScheduleRepository;

    private WagonEntityRepository wagonEntityRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        trainEntityRepository = mock(TrainEntityRepository.class);
        stationEntityRepository = mock(StationEntityRepository.class);
        pointOfScheduleRepository = mock(PointOfScheduleRepository.class);
        wagonEntityRepository = mock(WagonEntityRepository.class);

        trainService = new TrainService(trainEntityRepository, stationEntityRepository, pointOfScheduleRepository, wagonEntityRepository);
    }

    private TrainDto createTrainDto() throws Exception {
        mapper.registerModule(new JSR310Module());
        URL resource = getClass().getClassLoader().getResource("train_dto.json");
        Path path = Paths.get(resource.toURI());
        TrainDto trainDto = mapper.readValue(path.toFile(), TrainDto.class);
        return trainDto;
    }

    @Test
    void addTrain() throws Exception {
        when(trainEntityRepository.save(ArgumentMatchers.any(TrainEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(stationEntityRepository.findByNameStation(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(new StationEntity()));
        trainService.addTrain(createTrainDto());
        verify(trainEntityRepository, times(2)).save(ArgumentMatchers.any(TrainEntity.class));
        verify(stationEntityRepository, times(2)).findByNameStation(ArgumentMatchers.any(String.class));
    }

    @Test
    void findAllStartEndTimePeriod() {
        when(pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(LocalDateTime.class),
                        ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<PointOfScheduleEntity>());
        when(pointOfScheduleRepository
                .findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(LocalDateTime.class))).thenReturn(new ArrayList<PointOfScheduleEntity>());
       List<TrainDto> trains =  trainService.findAllStartEndTimePeriod("Test1","Test2", LocalDateTime.now().minusHours(1),LocalDateTime.now());
        verify(pointOfScheduleRepository, times(1)).findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(LocalDateTime.class),
                ArgumentMatchers.any(LocalDateTime.class));
        verify(pointOfScheduleRepository, times(1)).findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(LocalDateTime.class));

        assertTrue(trains.size() == 0);
    }

    @Test
    void findAllStartEndTimePeriodTransfer() {
        when(pointOfScheduleRepository
                .findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(LocalDateTime.class),
                        ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<PointOfScheduleEntity>());
        when(pointOfScheduleRepository
                .findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(LocalDateTime.class))).thenReturn(new ArrayList<PointOfScheduleEntity>());
        List<TransferDto> transfers =  trainService.findAllStartEndTimePeriodTransfer("Test1","Test2", LocalDateTime.now().minusHours(1),LocalDateTime.now());
        verify(pointOfScheduleRepository, times(1)).findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(LocalDateTime.class),
                ArgumentMatchers.any(LocalDateTime.class));
        verify(pointOfScheduleRepository, times(1)).findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(LocalDateTime.class));

        assertTrue(transfers.size() == 0);

    }

    @Test
    void getEmptySeats() {
        when(trainEntityRepository.findByTrainNumber(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(new TrainEntity()));
        when(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(
                ArgumentMatchers.any(TrainEntity.class),
                ArgumentMatchers.any(String.class)))
                .thenReturn(Optional.of(new PointOfScheduleEntity()));
        List<SeatDto> seatDtos = trainService.getEmptySeats(1L,"Test1","Test2",1L);
        verify(pointOfScheduleRepository, times(2))
                .findByTrainEntityAndStationEntityNameStation(ArgumentMatchers.any(TrainEntity.class),ArgumentMatchers.any(String.class));
        verify(trainEntityRepository, times(1)).findByTrainNumber(ArgumentMatchers.any(Long.class));
        assertTrue(seatDtos.size()==0);

    }

    @Test
    void getAllTrains() {
        when(trainEntityRepository.findByArrivalTimeEndAfter(ArgumentMatchers.any(LocalDateTime.class),ArgumentMatchers.any(Pageable.class)))
                .thenReturn(mock(Page.class));
        when(trainEntityRepository.findByArrivalTimeEndBeforeOrderByDepartureTimeDesc(ArgumentMatchers.any(LocalDateTime.class),ArgumentMatchers.any(Pageable.class)))
                .thenReturn(mock(Page.class));
        when(trainEntityRepository.findAllByOrderByDepartureTimeDesc(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(mock(Page.class));
        trainService.getAllTrains("act",1,1);
        trainService.getAllTrains("past",1,1);
        trainService.getAllTrains("all",1,1);
        verify(trainEntityRepository,times(1)).findByArrivalTimeEndAfter(ArgumentMatchers.any(LocalDateTime.class),ArgumentMatchers.any(Pageable.class));
        verify(trainEntityRepository,times(1)).findByArrivalTimeEndBeforeOrderByDepartureTimeDesc(ArgumentMatchers.any(LocalDateTime.class),ArgumentMatchers.any(Pageable.class));
        verify(trainEntityRepository,times(1)).findAllByOrderByDepartureTimeDesc(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void getTrainSchedule() {
        when(pointOfScheduleRepository
                .findAllByArrivalTimeAfterOrderByArrivalTime(ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(pointOfScheduleRepository
                .findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(ArgumentMatchers.any(String.class),ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        trainService.getTrainSchedule("all");
        verify(pointOfScheduleRepository,times(1)).findAllByArrivalTimeAfterOrderByArrivalTime(ArgumentMatchers.any(LocalDateTime.class));
        verify(pointOfScheduleRepository,times(0)).findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(ArgumentMatchers.any(String.class),ArgumentMatchers.any(LocalDateTime.class));
    }




    @Test
    void getAllActTrains() {
        when(trainEntityRepository.findAll()).thenReturn(new ArrayList<>());
        trainService.getAllActTrains();
        verify(trainEntityRepository, times(1)).findAll();
    }

    @Test
    void findAllWagon() {
        when(wagonEntityRepository.findAll()).thenReturn(new ArrayList<>());
        trainService.findAllWagon();
        verify(wagonEntityRepository,times(1)).findAll();
    }

    @Test
    void findTrain() {
        when(trainEntityRepository.findByTrainNumber(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new TrainEntity()));
        trainService.findTrain(ArgumentMatchers.any(Long.class));
        verify(trainEntityRepository,times(1)).findByTrainNumber(ArgumentMatchers.any(Long.class));
    }

    @Test
    void rollBackTrain() throws Exception {
        when(trainEntityRepository.save(ArgumentMatchers.any(TrainEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(pointOfScheduleRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new PointOfScheduleEntity()));
        when(trainEntityRepository.findByTrainNumber(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new TrainEntity()));
        trainService.rollBackTrain(createTrainDto());
        verify(pointOfScheduleRepository,times(2)).findById(ArgumentMatchers.any(Long.class));
        verify(pointOfScheduleRepository,times(2)).save(ArgumentMatchers.any(PointOfScheduleEntity.class));
    }
}
