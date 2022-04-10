package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WagonDto {
    private Long id;
    private Long wagonNumber;
    private String type;
    private String name;
    private Long sumSeats;
    private Long trainNumber;

}
