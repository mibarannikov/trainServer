package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {


    public final StationService stationService;

    public final ResponseErrorValidation responseErrorValidation;

    public final StationFacade stationFacade;

    public AdminController(StationService stationService, ResponseErrorValidation responseErrorValidation, StationFacade stationFacade) {
        this.stationService = stationService;
        this.responseErrorValidation = responseErrorValidation;
        this.stationFacade = stationFacade;
    }

    @PostMapping("/station/add")
    public ResponseEntity<Object> addStation(@Valid @RequestBody StationDto stationDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        StationEntity station = stationService.addStation(stationDto);


        return new ResponseEntity<>(stationFacade.stationToStationDto(station), HttpStatus.OK);
    }
}
