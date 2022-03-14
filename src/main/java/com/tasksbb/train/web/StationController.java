package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/station")
@CrossOrigin
@PreAuthorize("permitAll()")
public class StationController {

    public final StationService stationService;
    public final StationFacade stationFacade;

    public StationController(StationService stationService, StationFacade stationFacade) {
        this.stationService = stationService;
        this.stationFacade = stationFacade;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StationDto>> getAllStations() {
        List<StationDto> stationsDto = stationService.findAllStation()
                .stream().map(stationFacade::stationToStationDto).collect(Collectors.toList());
        return new ResponseEntity<>(stationsDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<StationDto> getStationByName(@RequestParam String name) {
        StationDto stationDto = stationService.findByNameStation(name);

        return new ResponseEntity<>(stationDto, HttpStatus.OK);
    }
}
