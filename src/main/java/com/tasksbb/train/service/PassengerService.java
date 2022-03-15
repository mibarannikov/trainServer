package com.tasksbb.train.service;

import com.tasksbb.train.dto.PassengerDto;
import com.tasksbb.train.entity.*;
import com.tasksbb.train.repository.PassengerEntityRepository;
import com.tasksbb.train.repository.SeatEntityRepository;
import com.tasksbb.train.repository.TicketEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {

    public final TicketEntityRepository ticketEntityRepository;
    public final PassengerEntityRepository passengerEntityRepository;
    public final TrainEntityRepository trainEntityRepository;
    public final SeatEntityRepository seatEntityRepository;

    public PassengerService(TicketEntityRepository ticketEntityRepository, PassengerEntityRepository passengerEntityRepository, TrainEntityRepository trainEntityRepository, SeatEntityRepository seatEntityRepository) {
        this.ticketEntityRepository = ticketEntityRepository;
        this.passengerEntityRepository = passengerEntityRepository;
        this.trainEntityRepository = trainEntityRepository;
        this.seatEntityRepository = seatEntityRepository;
    }

    public Boolean passengerIsPresent(TrainEntity train, PassengerEntity passenger) {
        List<SeatEntity> seats = seatEntityRepository.findByTrainEntity(train);
        for (SeatEntity st : seats) {
            for (TicketEntity tk : st.getTickets()) {
                if (tk.getPassengerEntity().equals(passenger) && tk.getPointOfSchedules().get(tk.getPointOfSchedules().size() - 1).getArrivalTime().isAfter(LocalDateTime.now())) {
                    return true;
                }
            }
        }
        return false;
    }




}
