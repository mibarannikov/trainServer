package com.tasksbb.train.service;

import com.tasksbb.train.dto.PointOfScheduleDto;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.PassengerEntity;
import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.User;
import com.tasksbb.train.facade.TicketFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.SeatEntityRepository;
import com.tasksbb.train.repository.TicketEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {


    private final SeatEntityRepository seatEntityRepository;

    private final TicketEntityRepository ticketEntityRepository;

    private final PointOfScheduleRepository pointOfScheduleRepository;


    public TicketService(SeatEntityRepository seatEntityRepository,
                         TicketEntityRepository ticketEntityRepository,
                         PointOfScheduleRepository pointOfScheduleRepository
    ) {
        this.seatEntityRepository = seatEntityRepository;
        this.ticketEntityRepository = ticketEntityRepository;
        this.pointOfScheduleRepository = pointOfScheduleRepository;

    }

    @Transactional
    public TicketDto buyTicket(TicketDto ticketDto, User user) {
        TicketEntity newTicket = new TicketEntity();
        SeatEntity seat = seatEntityRepository
                .findByTrainEntityTrainNumberAndSeatNumber(ticketDto.getNumberTrainOwner(), ticketDto.getSeatNumber());// todo orElseThrow()
        PassengerEntity passenger = new PassengerEntity();
        passenger.setFirstname(ticketDto.getFirstnamePassenger());
        passenger.setLastname(ticketDto.getLastnamePassenger());
        passenger.setDateOfBirth(ticketDto.getDateOfBirth());
        newTicket.setUser(user);
        newTicket.setPassengerEntity(passenger);
        newTicket.setSeatEntity(seat);
        for (PointOfScheduleDto name : ticketDto.getNameStations()) {
            newTicket.getPointOfSchedules()
                    .add(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(seat.getTrainEntity(), name.getNameStation()));// todo orElseThrow()
        }
        newTicket.getPointOfSchedules().remove(newTicket.getPointOfSchedules().size()-1);
        newTicket = ticketEntityRepository.save(newTicket);
        return TicketFacade.ticketToTicketDto(newTicket);
    }

    public List<TicketDto> getAllUserTickets(User user){
        List<TicketEntity> tickets = ticketEntityRepository.findAllByUser(user);
        return tickets.stream().map(TicketFacade::ticketToTicketDto).collect(Collectors.toList());


    }

}
