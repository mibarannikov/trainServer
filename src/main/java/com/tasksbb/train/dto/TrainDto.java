package com.tasksbb.train.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainDto {
    private int id;
    private int trainNumber;
    private String startStation;
    private String stopStation;
    private int seatCapacity;
    private LocalDateTime start;
    private LocalDateTime stop;
}

