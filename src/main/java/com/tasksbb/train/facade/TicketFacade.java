package com.tasksbb.train.facade;

import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.WagonEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TicketFacade {
    public static TicketDto ticketToTicketDto(TicketEntity ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setFirstnamePassenger(ticket.getPassengerEntity().getFirstname());
        ticketDto.setLastnamePassenger(ticket.getPassengerEntity().getLastname());
        ticketDto.setDateOfBirth(ticket.getPassengerEntity().getDateOfBirth());
        ticketDto.setNumberTrainOwner(ticket.getSeatEntity().getTrainEntity().getTrainNumber());
        Long seatNumberDto = ticket.getSeatEntity().getSeatNumber();
        Long wagonNumber = 0L;
        for (WagonEntity w: ticket.getSeatEntity().getTrainEntity().getWagonEntities()){
            seatNumberDto-=w.getSumSeats();
            if (seatNumberDto<=0){
                seatNumberDto+=w.getSumSeats();
                wagonNumber=w.getWagonNumber();
                break;
            }
        }
        ticketDto.setSeatNumber(seatNumberDto);
        ticketDto.setWagonNumber(wagonNumber);
        ticketDto.setNameStations(ticket.getPointOfSchedules()
                .stream()
                .map(PointOfScheduleFacade::pointEntityToPointDto)
                .collect(Collectors.toList()));
        return ticketDto;
    }
}
