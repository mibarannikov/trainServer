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
    public Long id;
    @NotEmpty
    public Long seatNumber;
    @NotEmpty
    public String firstnamePassenger;
    @NotEmpty
    public String lastnamePassenger;
    @NotEmpty
    public LocalDate dateOfBirth;
    @NotEmpty
    public Long numberTrainOwner;
    @NotEmpty
    public List<PointOfScheduleDto> nameStations = new ArrayList<>();
}
