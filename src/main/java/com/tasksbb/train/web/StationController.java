package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/station")
@CrossOrigin
@PreAuthorize("permitAll()")
public class StationController {

    public final StationService stationService;
    public final TrainService trainService;

    @GetMapping("/all")
    public ResponseEntity<List<StationDto>> getAllStations() {
        List<StationDto> stationsDto = stationService.findAllStation()
                .stream().map(StationFacade::stationToStationDto).collect(Collectors.toList());
        return new ResponseEntity<>(stationsDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StationDto>> getAllSearchStations(@RequestParam(name = "value") String value) {
        List<StationDto> stationsDto = stationService.findAllSearchStation(value);
        return new ResponseEntity<>(stationsDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<StationDto> getStationByName(@RequestParam String name) {
        StationDto stationDto = stationService.findByNameStation(name);

        return new ResponseEntity<>(stationDto, HttpStatus.OK);
    }

    @GetMapping("/stationschedule")
    public ResponseEntity<List<TrainDto>> stationSchedule(@RequestParam(name = "station") String nameStation) {

        List<TrainDto> trains = trainService.getTrainSchedule(nameStation);

        return new ResponseEntity<>(trains, HttpStatus.OK);
    }
}
