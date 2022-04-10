package com.tasksbb.train.web;

import com.tasksbb.train.service.TrainService;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @MockBean
    private TrainService trainService;

    @Test
    void trains() throws Exception {
        mockMvc.perform(get("/api/train/search").param("start","testStation1")
                .param("end", "testStation2")
                .param("tpstart","2022-04-10T14:47:15" )
                .param("tpend", "2022-05-10T14:47:15"))
                .andExpect(status().isOk());

    }

    @Test
    void trainsWithTransfer() throws Exception {
        mockMvc.perform(get("/api/train/searchtransfer").param("start","testStation1")
                        .param("end", "testStation2")
                        .param("tpstart","2022-04-10T14:47:15" )
                        .param("tpend", "2022-05-10T14:47:15"))
                .andExpect(status().isOk());
    }
}
