package com.tasksbb.train.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.tasksbb.train.dto.PageDto;
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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final StationService stationService;

    private final ResponseErrorValidation responseErrorValidation;

    private final JmsTemplate jmsTemplate;

    private final TrainService trainService;

    private final TicketService ticketService;


    @PostMapping("/station/add")
    public ResponseEntity<StationDto> addStation(@RequestBody StationDto stationDto,
                                                 BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        StationEntity station = stationService.addStation(stationDto);
        jmsTemplate.convertAndSend("one", "update");
        log.debug("message sent update in ActiveMQ destination one from AdminController.addStation(), time {} ", LocalDateTime.now());
        return new ResponseEntity<>(StationFacade.stationToStationDto(station), HttpStatus.OK);
    }

    @PutMapping("/station/edit")
    public ResponseEntity<StationDto> editStation(@RequestBody StationDto stationDto,
                                                  BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        StationDto station = stationService.editStation(stationDto);
       jmsTemplate.convertAndSend("one", "update");
        log.debug("message sent update in ActiveMQ destination one from AdminController.editStation(), time {} ", LocalDateTime.now());
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    @PutMapping("/train/rollback")
    public ResponseEntity<TrainDto> rollBackTrain(@RequestBody TrainDto trainDto, BindingResult bindingResult) throws JsonProcessingException {
        TrainDto train = trainService.rollBackTrain(trainDto);
        Gson gson = new Gson();
        String trainJson = gson.toJson(train);
        LOG.info(trainJson);
        jmsTemplate.convertAndSend("one", "update");
        log.debug("message sent update in ActiveMQ destination one from AdminController.rollBackTrain(), time {} ", LocalDateTime.now());
        return new ResponseEntity<>(train, HttpStatus.OK);
    }

    @PutMapping("/train/update")
    public ResponseEntity<TrainDto> updateTrain(@RequestBody TrainDto trainDto, BindingResult bindingResult) {
        TrainDto train = TrainFacade.trainToDto(trainService.updateTrain(trainDto));
        jmsTemplate.convertAndSend("one", "update");
        log.debug("message sent update in ActiveMQ destination one from AdminController.updateTrain(), time {} ", LocalDateTime.now());
        return new ResponseEntity<>(train, HttpStatus.OK);
    }

    @PostMapping("/train/add")
    public ResponseEntity<TrainDto> addTrain(@RequestBody TrainDto trainDto,
                                             BindingResult bindingResult) {
        responseErrorValidation.mapValidationService(bindingResult);
//        ResponseEntity<Object> errors =
//        if (!ObjectUtils.isEmpty(errors)) {
//            return errors;
//        }
        TrainEntity train = trainService.addTrain(trainDto);
        jmsTemplate.convertAndSend("one", "update");
        log.debug("message sent update in ActiveMQ destination one from AdminController.addTrain(), time {} ", LocalDateTime.now());
        return new ResponseEntity<>(TrainFacade.trainToDto(train), HttpStatus.OK);
    }

    @GetMapping("/train/all")
    public ResponseEntity<PageDto> getAllTrainsFromPast(@RequestParam(name = "param") String param,
                                                        @RequestParam(name = "page") int page,
                                                        @RequestParam(name = "amount") int amount) {
        PageDto trains = trainService.getAllTrains(param, page, amount);
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/train/allact")
    public ResponseEntity<List<TrainDto>> getAllActTrains() {
        return new ResponseEntity<>(trainService.getAllActTrains(), HttpStatus.OK);
    }

    @GetMapping("/alltickets")
    public ResponseEntity<List<TicketDto>> getAllTrainTickets(@RequestParam(name = "train") Long trainNumber) {
        return new ResponseEntity<>(ticketService.AllTrainTickets(trainNumber), HttpStatus.OK);

    }

    @GetMapping("/regtickets")
    public ResponseEntity<List<TicketDto>> getRegTrainTickets(@RequestParam(name = "train") Long trainNumber) {

        return new ResponseEntity<>(ticketService.ticketsOnTheTrainNow(trainNumber), HttpStatus.OK);
    }

    @GetMapping("/train/find")
    public ResponseEntity<TrainDto> train(@RequestParam(name = "trainNumber") Long trainNumber) {

        return new ResponseEntity<>(trainService.findTrain(trainNumber), HttpStatus.OK);
    }
}
