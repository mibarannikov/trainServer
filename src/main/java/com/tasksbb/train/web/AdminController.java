package com.tasksbb.train.web;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.facade.TrainFacade;
import com.tasksbb.train.service.StationService;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.TrainService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {


    public final StationService stationService;

    public final ResponseErrorValidation responseErrorValidation;


    public final TrainService trainService;

    public final TicketService ticketService;


    @PostMapping("/station/add")
    public ResponseEntity<StationDto> addStation( @RequestBody StationDto stationDto,
                                             BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        StationEntity station = stationService.addStation(stationDto);
        return new ResponseEntity<>(StationFacade.stationToStationDto(station), HttpStatus.OK);
    }

    @PutMapping("/station/edit")
    public ResponseEntity<StationDto> editStation( @RequestBody StationDto stationDto,
                                                  BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        StationDto station = stationService.editStation(stationDto);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }


    @PostMapping("/train/add")
    public ResponseEntity<TrainDto> addTrain( @RequestBody TrainDto trainDto,
                                           BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        ResponseEntity<Object> errors =
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        TrainEntity train = trainService.addTrain(trainDto);
        return new ResponseEntity<>(TrainFacade.trainToDto(train), HttpStatus.OK);
    }

    @GetMapping("/train/all")
    public ResponseEntity<List<TrainDto>> getAllTrainsFromPast(@RequestParam(name="param") String param) {
        List<TrainDto> trains = trainService.getAllTrains(param);
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/train/allact")
    public  ResponseEntity<List<TrainDto>> getAllActTrains(){
        return new ResponseEntity<>(trainService.getAllActTrains(),HttpStatus.OK);
    }

    @GetMapping("/alltickets")
    public ResponseEntity<List<TicketDto>> getAllTrainTickets(@RequestParam(name = "train") Long trainNumber){
        return new ResponseEntity<>(ticketService.AllTrainTickets(trainNumber), HttpStatus.OK);

    }
    @GetMapping("/regtickets")
    public ResponseEntity<List<TicketDto>> getRegTrainTickets(@RequestParam(name = "train") Long trainNumber) {

        return new ResponseEntity<>(ticketService.ticketsOnTheTrainNow(trainNumber), HttpStatus.OK);
    }

}
