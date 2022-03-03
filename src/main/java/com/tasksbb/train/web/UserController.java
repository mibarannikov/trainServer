package com.tasksbb.train.web;

import com.tasksbb.train.dto.UserDTO;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.facade.UserFacade;
import com.tasksbb.train.service.UserService;
import com.tasksbb.train.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {


    private final UserService userService;

    private final UserFacade userFacade;

    private final ResponseErrorValidation responseErrorValidation;

    public UserController(UserService userService,
                          UserFacade userFacade,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
