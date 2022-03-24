package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketDto {
    private Long id;
    @NotEmpty
    private Long seatNumber;
    @NotEmpty
    private String firstnamePassenger;
    @NotEmpty
    private String lastnamePassenger;
    @NotEmpty
    private LocalDate dateOfBirth;
    @NotEmpty
    private Long numberTrainOwner;
    @NotEmpty
    private List<PointOfScheduleDto> nameStations = new ArrayList<>();
}
