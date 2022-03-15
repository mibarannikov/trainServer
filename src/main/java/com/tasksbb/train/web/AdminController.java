package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.TrainService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {


    public final StationService stationService;

    public final ResponseErrorValidation responseErrorValidation;

    public final StationFacade stationFacade;

    public final TrainService trainService;

    public final TicketService ticketService;

    public AdminController(StationService stationService, ResponseErrorValidation responseErrorValidation, StationFacade stationFacade, TrainService trainService, TicketService ticketService) {
        this.stationService = stationService;
        this.responseErrorValidation = responseErrorValidation;
        this.stationFacade = stationFacade;
        this.trainService = trainService;
        this.ticketService = ticketService;
    }

    @PostMapping("/station/add")
    public ResponseEntity<Object> addStation(@RequestBody StationDto stationDto,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        StationEntity station = stationService.addStation(stationDto);
        return new ResponseEntity<>(stationFacade.stationToStationDto(station), HttpStatus.OK);
    }

    @PostMapping("/train/add")
    public ResponseEntity<Object> addTrain(@RequestBody TrainDto trainDto,
                                           BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        TrainEntity train = trainService.addTrain(trainDto);
        return new ResponseEntity<>(train.getTrainNumber(), HttpStatus.OK);
    }

    @GetMapping("/train/all")
    public ResponseEntity<Object> getAllTrains() {
        List<TrainDto> trains = trainService.getAllTrains();
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/regtickets")
    public ResponseEntity<Object> getRegTicketsOfTrain(@RequestParam(name = "train") Long trainNumber) {

        return new ResponseEntity<>(ticketService.ticketsOnTheTrainNow(trainNumber), HttpStatus.OK);
    }

}
