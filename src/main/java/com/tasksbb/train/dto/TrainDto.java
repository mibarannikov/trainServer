package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TrainDto {
    public Long id;
    @NotEmpty
    private Long trainNumber;
    @NotEmpty
    private LocalDateTime departureTime;
    @NotEmpty
    private LocalDateTime arrivalTimeEnd;
    @NotEmpty
    private Double trainSpeed;
    @NotEmpty
    private Long sumSeats;
    private Long amountOfEmptySeats;
    @NotEmpty
    private List<PointOfScheduleDto> pointsOfSchedule = new ArrayList<>();
}

