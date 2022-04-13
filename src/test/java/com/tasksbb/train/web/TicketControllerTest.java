package com.tasksbb.train.web;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {
    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainEntityRepository trainEntityRepository;
    @MockBean
    private PointOfScheduleRepository pointOfScheduleRepository;

    @Test
    void buyTicket() {
    }

    @Test
    void emptySeats() throws Exception {
        TrainEntity train = new TrainEntity();
        train.setTrainNumber(10L);
        when(trainEntityRepository.findByTrainNumber(anyLong())).thenReturn(Optional.of(train));
        when(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(any(TrainEntity.class),anyString()))
                .thenReturn(Optional.of(new PointOfScheduleEntity()));
        mockMvc.perform(get("/api/ticket/searchseats")
                        .param("train", "10")
                        .param("wagon", "0")
                        .param("start", "test")
                        .param("end", "test1"))
                .andExpect(status().isOk());

    }


}
