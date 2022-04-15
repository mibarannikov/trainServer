package com.tasksbb.train.web;

import com.tasksbb.train.dto.PriceDto;
import com.tasksbb.train.dto.SeatDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.facade.TicketFacade;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.TrainService;
import com.tasksbb.train.service.UserService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/ticket")
@CrossOrigin
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class TicketController {


    private final UserService userService;

    private final ResponseErrorValidation responseErrorValidation;

    private final TicketService ticketService;

    private final TrainService trainService;


    @PostMapping("/buyticket")
    ResponseEntity<Object> buyTicket(@RequestBody TicketDto ticket, BindingResult bindingResult, Principal principal) {
        responseErrorValidation.mapValidationService(bindingResult);
//        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) return errors;
        User user = userService.getCurrentUser(principal);
        TicketDto boughtTicket = TicketFacade.ticketToTicketDto(ticketService.buyTicket(ticket, user));
        return new ResponseEntity<>(boughtTicket, HttpStatus.OK);
    }

    @GetMapping("/searchseats")
    ResponseEntity<List<SeatDto>> emptySeats(@RequestParam(name = "train") Long trainNumber,
                                             @RequestParam(name = "wagon") Long wagonNumber,
                                             @RequestParam(name = "start") String startStation,
                                             @RequestParam(name = "end"  ) String endStation) {
        List<SeatDto> seats = trainService.getEmptySeats(trainNumber, startStation, endStation, wagonNumber);
        return new ResponseEntity<>(seats, HttpStatus.OK);

    }

    @GetMapping("/ticketprice")
    ResponseEntity<PriceDto> ticketPrice(@RequestParam(name = "train") Long trainNumber,
                                         @RequestParam(name = "wagon") Long wagonNumber,
                                         @RequestParam(name = "start") String startStation,
                                         @RequestParam(name = "end") String endStation) {
        String price = ticketService.priceCalculation(trainNumber, wagonNumber, startStation, endStation);
        PriceDto pr = new PriceDto();
        pr.setPrice(price);
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }


}
