package com.tasksbb.train.service;

import com.tasksbb.train.config.StationConfigTest;
import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.repository.StationEntityRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = StationConfigTest.class,
//        loader = AnnotationConfigContextLoader.class)
public class StationServiceTest {

//    @Autowired
//    private StationService stationService;
//
    @MockBean
    private StationEntityRepository stationEntityRepository;

    private static final String nameStation = "Москва";

    @Test
    public void notFoundStation(){
        StationService stationService = new StationService();
        Mockito.when(stationEntityRepository.findByNameStation(nameStation)).thenReturn(null);
        StationDto stationDto = stationService.findByNameStation(nameStation);
        Assertions.assertNull(stationDto);

    }

}
