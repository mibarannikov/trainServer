package com.tasksbb.train.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class PointOfScheduleDto {
    private Long id;
    private String nameStation;
    private LocalDateTime arrivalTime;

}
