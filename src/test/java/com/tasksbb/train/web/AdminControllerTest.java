package com.tasksbb.train.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.service.TrainService;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrainService trainService;
    @Autowired
    private StationService stationService;

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    private ObjectMapper mapper = new ObjectMapper();

    private TrainDto createTrainDto() throws Exception {
        mapper.registerModule(new JSR310Module());
        URL resource = getClass().getClassLoader().getResource("train_dto.json");
        Path path = Paths.get(resource.toURI());
        TrainDto trainDto = mapper.readValue(path.toFile(), TrainDto.class);
        return trainDto;
    }

    private StationDto createStationDto(String file) throws URISyntaxException, IOException {
        mapper.registerModule(new JSR310Module());
        URL resource = getClass().getClassLoader().getResource(file);
        Path path = Paths.get(resource.toURI());
        StationDto stationDto = mapper.readValue(path.toFile(), StationDto.class);
        return stationDto;

    }
    private byte[] createJson(String file) throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        Path path = Paths.get(resource.toURI());
        return Files.readAllBytes(path);

    }



    @BeforeEach
    private void init() throws Exception {

    }

    @Test
    void addStation() throws Exception {

    }

    @Test
    void editStation() throws Exception {
        mockMvc.perform(post("/api/admin//station/add").contentType(MediaType.APPLICATION_JSON).content(createJson("station_dto.json")))
                .andExpect(status().isOk());
    }

    @Test
    void addTrain() throws Exception {
        stationService.addStation(createStationDto("station_dto.json"));
        stationService.addStation(createStationDto("station_dto1.json"));
        mockMvc.perform(post("/api/admin/train/add").contentType(MediaType.APPLICATION_JSON).content(createJson("train_dto.json")))
                .andExpect(status().isOk());
    }

//    @Test
//    void updateTrain() throws Exception {
//        stationService.addStation(createStationDto("station_dto.json"));
//        stationService.addStation(createStationDto("station_dto1.json"));
//        trainService.addTrain(createTrainDto());
//        mockMvc.perform(put("/api/admin/train/update").contentType(MediaType.APPLICATION_JSON).content(createJson("train_dto_update.json")))
//                .andExpect(status().isOk());
//    }





    @Test
    void getAllTrainsFromPast() throws Exception {
        mockMvc.perform(get("/api/admin/train/all").param("param", "act")
                        .param("page", "1")
                        .param("amount", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllActTrains() throws Exception {
        mockMvc.perform(get("/api/admin/train/allact"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllTrainTickets() throws Exception {
        mockMvc.perform(get("/api/admin/alltickets").param("train", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegTrainTickets() throws Exception {
        mockMvc.perform(get("/api/admin/regtickets").param("train", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void train() throws Exception {
        stationService.addStation(createStationDto("station_dto.json"));
        stationService.addStation(createStationDto("station_dto1.json"));
        trainService.addTrain(createTrainDto());
        mockMvc.perform(get("/api/admin/train/find").param("trainNumber", "1"))
                .andExpect(status().isOk());
    }
}
