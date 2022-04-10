package com.tasksbb.train.web;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
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
class TicketControllerTest {
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buyTicket() {
    }

    @Test
    void emptySeats() throws Exception {
        mockMvc.perform(get("/api/ticket/searchseats")
                        .param("train","10")
                        .param("wagon","0")
                        .param("start","test" )
                        .param("end"  ,"test1"))
                .andExpect(status().isOk());

    }


}
