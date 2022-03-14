package com.tasksbb.train.dto;

import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.TrainEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class SeatEntityDto {

    private Long id;
    private Long seatNumber;
    private Long trainNumber;
    private List<TicketDto> tickets = new ArrayList<>();
}
