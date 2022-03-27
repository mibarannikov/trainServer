package com.tasksbb.train.facade;

import com.tasksbb.train.dto.SeatDto;
import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.WagonEntity;

import java.util.stream.Collectors;

public class SeatFacade {
    public static SeatDto seatToSeatDto(SeatEntity seat){
        SeatDto seatDto = new SeatDto();
        seatDto.setId(seat.getId());
        Long seatNumberDto = seat.getSeatNumber();
        Long wagonNumber = 0L;
        for (WagonEntity w: seat.getTrainEntity().getWagonEntities()){
            seatNumberDto-=w.getSumSeats();
            if (seatNumberDto<=0){
                seatNumberDto+=w.getSumSeats();
                wagonNumber=w.getWagonNumber();
                break;
            }
        }
        seatDto.setSeatNumber(seatNumberDto);
        seatDto.setWagonNumber(wagonNumber);
        seatDto.setTrainNumber(seat.getTrainEntity().getTrainNumber());
        seatDto.setTickets(seat.getTickets()
                .stream()
                .map(TicketFacade::ticketToTicketDto).collect(Collectors.toList()));
        return seatDto;

    }
}
