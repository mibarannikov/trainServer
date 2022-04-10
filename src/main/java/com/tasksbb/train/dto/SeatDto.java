package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeatDto {

    private Long id;
    private Long seatNumber;
    private Long wagonNumber;
    private Long trainNumber;
    private List<TicketDto> tickets = new ArrayList<>();
}
