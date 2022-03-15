package com.tasksbb.train.web;

import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.dto.UserDTO;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.facade.UserFacade;
import com.tasksbb.train.service.TicketService;
import com.tasksbb.train.service.UserService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {


    private final UserService userService;

    private final UserFacade userFacade;

    private final TicketService ticketService;

    public UserController(UserService userService,
                          UserFacade userFacade,
                          ResponseErrorValidation responseErrorValidation,
                          TicketService ticketService) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> getAllTicketsForUser(@RequestParam(name="param") String param, Principal principal) {
        User user = userService.getCurrentUser(principal);
        List<TicketDto> ticketsUser = ticketService.getAllUserTickets(user, param);
        return new ResponseEntity<>(ticketsUser, HttpStatus.OK);
    }

}
