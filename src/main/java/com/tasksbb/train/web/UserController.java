package com.tasksbb.train.web;

import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.dto.UserDto;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.facade.UserFacade;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.UserService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final TicketService ticketService;

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDTO = UserFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> getAllTicketsForUser(@RequestParam(name = "param") String param, Principal principal) {
        User user = userService.getCurrentUser(principal);
        List<TicketDto> ticketsUser = ticketService.getAllUserTickets(user, param);
        return new ResponseEntity<>(ticketsUser, HttpStatus.OK);
    }

}
