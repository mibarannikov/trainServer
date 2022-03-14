package com.tasksbb.train.facade;

import com.tasksbb.train.dto.SeatEntityDto;
import com.tasksbb.train.entity.SeatEntity;

import java.util.stream.Collectors;

public class SeatFacade {
    public static SeatEntityDto seatToSeatDto(SeatEntity seat){
        SeatEntityDto seatDto = new SeatEntityDto();
        seatDto.setId(seat.getId());
        seatDto.setSeatNumber(seat.getSeatNumber());
        seatDto.setTrainNumber(seat.getTrainEntity().getTrainNumber());
        seatDto.setTickets(seat.getTickets()
                .stream()
                .map(TicketFacade::ticketToTicketDto).collect(Collectors.toList()));
        return seatDto;

    }
}
