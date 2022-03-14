package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketDto {
public Long id;
public Long seatNumber;
public String firstnamePassenger;
public String lastnamePassenger;
public LocalDate dateOfBirth;
public Long numberTrainOwner;
public List<PointOfScheduleDto> nameStations = new ArrayList<>();
}
