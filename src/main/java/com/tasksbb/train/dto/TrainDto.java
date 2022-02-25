package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrainDto {
    private int id;
    private int trainNumber;
    private String startStation;
    private String stopStation;
    private int seatCapacity;
    private LocalDateTime start;
    private LocalDateTime stop;
}

