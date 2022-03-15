package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PassengerDto {
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDateTime dateOfBirth;
}
