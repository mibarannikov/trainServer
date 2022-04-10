package com.tasksbb.train.facade;

import com.tasksbb.train.dto.UserDto;
import com.tasksbb.train.entity.User;
import org.springframework.stereotype.Component;


public class UserFacade {

    public static UserDto userToUserDTO(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

}
