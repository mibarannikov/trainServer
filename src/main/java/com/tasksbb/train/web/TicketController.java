package com.tasksbb.train.web;

import com.tasksbb.train.dto.SeatEntityDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.TrainService;
import com.tasksbb.train.service.UserService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/ticket")
@CrossOrigin
@PreAuthorize("permitAll()")
public class TicketController {


    private final UserService userService;

    private final ResponseErrorValidation responseErrorValidation;

    private final TicketService ticketService;

    private  final TrainService trainService;

    public TicketController(UserService userService, ResponseErrorValidation responseErrorValidation, TicketService ticketService, TrainService trainService) {
        this.userService = userService;
        this.responseErrorValidation = responseErrorValidation;
        this.ticketService = ticketService;
        this.trainService = trainService;
    }

    @PostMapping("/buyticket")
    ResponseEntity<Object> buyTicket(@RequestBody TicketDto ticket, BindingResult bindingResult, Principal principal) {
        responseErrorValidation.mapValidationService(bindingResult);
//        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) return errors;
        User user = userService.getCurrentUser(principal);

        TicketDto boughtTicket = ticketService.buyTicket(ticket, user);
        return new ResponseEntity<>(boughtTicket, HttpStatus.OK);
    }

    @GetMapping("/searchseats")
    ResponseEntity<List<SeatEntityDto>> emptySeats(@RequestParam(name="train") Long trainNumber,
                                                   @RequestParam(name = "start") String startStation,
                                                   @RequestParam(name= "end") String endStation){
        List<SeatEntityDto> seats = trainService.getEmptySeats(trainNumber,startStation,endStation);
       return new ResponseEntity<>(seats, HttpStatus.OK);

    }


}
