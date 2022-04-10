package com.tasksbb.train.dto;


import com.tasksbb.train.entity.enums.EStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class PointOfScheduleDto {
    private Long id;
    @NotEmpty
    private String nameStation;
    @NotEmpty
    private LocalDateTime arrivalTime;

    private LocalDateTime arrivalTimeInit;
    @NotEmpty
    private LocalDateTime departureTime;

    private LocalDateTime departureTimeInit;

    private EStatus delayed;
}
