package com.tasksbb.train.facade;

import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TicketEntity;

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
        ticketDto.setSeatNumber(ticket.getSeatEntity().getSeatNumber());
        ticketDto.setNumberTrainOwner(ticket.getSeatEntity().getTrainEntity().getTrainNumber());
//        List<PointOfScheduleEntity> points = new ArrayList<>();
//        for(PointOfScheduleEntity p: ticket.getSeatEntity().getTrainEntity().getPointOfSchedules()){
//            if(p.getArrivalTime().isAfter(ticket.getPointOfSchedules().get(ticket.getPointOfSchedules().size()-1).getArrivalTime())){
//                ticket.getPointOfSchedules().add(p);break;
//            }
//        }
        ticketDto.setNameStations(ticket.getPointOfSchedules()
                .stream()
                .map(PointOfScheduleFacade::pointEntityToPointDto)
                .collect(Collectors.toList()));
        return ticketDto;
    }
}
