package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TrainDto {
    public Long id;
    private Long trainNumber;
    private LocalDateTime departureTime;
    private Double trainSpeed;
    private Long sumSeats;
    private Long amountOfEmptySeats;
    private List<PointOfScheduleDto> pointsOfSchedule = new ArrayList<>();
}

