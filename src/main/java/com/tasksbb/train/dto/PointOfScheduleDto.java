package com.tasksbb.train.dto;




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
    @NotEmpty
    private LocalDateTime departureTime;
}
