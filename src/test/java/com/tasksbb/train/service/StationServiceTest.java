package com.tasksbb.train.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

class StationServiceTest {

    private StationService stationService;

    private StationEntityRepository stationEntityRepository;

    private PointOfScheduleRepository pointOfScheduleRepository;

    private TrainEntityRepository trainEntityRepository;

    private WagonEntityRepository wagonEntityRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    private void init() {
        stationEntityRepository = mock(StationEntityRepository.class);
        pointOfScheduleRepository = mock(PointOfScheduleRepository.class);
        trainEntityRepository = mock(TrainEntityRepository.class);
        wagonEntityRepository = mock(WagonEntityRepository.class);
        stationService = new StationService(stationEntityRepository,
                pointOfScheduleRepository,
                trainEntityRepository,
                wagonEntityRepository);
    }

    private StationDto createStationDto() throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource("station_dto1.json");
        Path path = Paths.get(resource.toURI());
        StationDto stationDto = mapper.readValue(path.toFile(), StationDto.class);
        return stationDto;
    }

    @Test
    void findAllStation() {
        when(stationEntityRepository.findByOrderByNameStationAsc()).thenReturn(new ArrayList<>());
        stationService.findAllStation();
        verify(stationEntityRepository, times(1)).findByOrderByNameStationAsc();
    }

    @Test
    void addStation() throws URISyntaxException, IOException {
        when(stationEntityRepository.findByNameStation(ArgumentMatchers.anyString())).thenReturn(Optional.of(new StationEntity()));
        when(stationEntityRepository.save(ArgumentMatchers.any(StationEntity.class))).thenReturn(new StationEntity());
        stationService.addStation(createStationDto());
        verify(stationEntityRepository, times(3)).findByNameStation(ArgumentMatchers.anyString());
        verify(stationEntityRepository, times(1)).save(ArgumentMatchers.any(StationEntity.class));
    }

    @Test
    void findByNameStation() {
        when(stationEntityRepository.findByNameStation(ArgumentMatchers.anyString())).thenReturn(Optional.of(new StationEntity()));
        stationService.findByNameStation(ArgumentMatchers.anyString());
        verify(stationEntityRepository, times(1)).findByNameStation(ArgumentMatchers.anyString());
    }

    @Test
    void findAllSearchStation() {
        when(stationEntityRepository.findByOrderByNameStationAsc()).thenReturn(new ArrayList<>());
        stationService.findAllSearchStation("all");
        verify(stationEntityRepository, times(1)).findByOrderByNameStationAsc();
    }

    @Test
    void editStation() throws URISyntaxException, IOException {
        StationEntity station = new StationEntity();
        station.setNameStation("test");
        when(stationEntityRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(station));
        when(stationEntityRepository.findByNameStation(ArgumentMatchers.anyString())).thenReturn(Optional.of(new StationEntity()));
        when(stationEntityRepository.save(ArgumentMatchers.any(StationEntity.class))).thenReturn(new StationEntity());
        stationService.editStation(createStationDto());
        verify(stationEntityRepository, times(1)).findById(ArgumentMatchers.anyLong());
        verify(stationEntityRepository, times(3)).findByNameStation(ArgumentMatchers.anyString());
    }


}
