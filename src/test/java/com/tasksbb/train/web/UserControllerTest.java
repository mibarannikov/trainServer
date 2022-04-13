package com.tasksbb.train.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.service.UserService;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.jeasy.random.EasyRandom;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void getCurrentUser() throws Exception {
        EasyRandom generator = new EasyRandom();
        User user = generator.nextObject(User.class);
        when(userService.getCurrentUser(null)).thenReturn(user);
        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk());
    }

}
