package com.tasksbb.train.web;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class StationControllerTest {
    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getAllStations() throws Exception {
        mockMvc.perform(get("/api/station/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllSearchStations() throws Exception {
        mockMvc.perform(get("/api/station/search").param("value","test"))
                .andExpect(status().isOk());


    }

//    @Test
//    void getStationByName() throws Exception {
//        mockMvc.perform(get("/api/station/get").param("name","test"))
//                .andExpect(status().isOk());
//    }

    @Test
    void stationSchedule() throws Exception {
        mockMvc.perform(get("/api/station/stationschedule").param("station","test"))
                .andExpect(status().isOk());

    }
}
