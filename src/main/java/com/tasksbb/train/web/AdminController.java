package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.service.TrainService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {


    public final StationService stationService;

    public final ResponseErrorValidation responseErrorValidation;

    public final StationFacade stationFacade;

    public final TrainService trainService;

    public AdminController(StationService stationService, ResponseErrorValidation responseErrorValidation, StationFacade stationFacade, TrainService trainService) {
        this.stationService = stationService;
        this.responseErrorValidation = responseErrorValidation;
        this.stationFacade = stationFacade;
        this.trainService = trainService;
    }

    @PostMapping("/station/add")
    public ResponseEntity<Object> addStation(@Valid @RequestBody StationDto stationDto,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        StationEntity station = stationService.addStation(stationDto);
        return new ResponseEntity<>(stationFacade.stationToStationDto(station), HttpStatus.OK);
    }

    @PostMapping("/train/add")
    public ResponseEntity<Object> addTrain(@Valid @RequestBody TrainDto trainDto,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        TrainEntity train = trainService.addTrain(trainDto);
        return new ResponseEntity<>(train.getTrainNumber(), HttpStatus.OK);
    }



}
