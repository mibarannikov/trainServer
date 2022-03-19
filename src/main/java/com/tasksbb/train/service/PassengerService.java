package com.tasksbb.train.service;

import com.tasksbb.train.entity.*;
import com.tasksbb.train.repository.PassengerEntityRepository;
import com.tasksbb.train.repository.SeatEntityRepository;
import com.tasksbb.train.repository.TicketEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Boolean passengerIsPresent(TrainEntity train, PassengerEntity passenger, List<PointOfScheduleEntity> points) {
        List<SeatEntity> seats = seatEntityRepository.findByTrainEntity(train);
//        long countTickets = ticketEntityRepository
//                .countByPassengerEntityAndPointOfSchedules_DepartureTimeAfterAndPointOfSchedules_ArrivalTimeBeforeAndPointOfSchedules_TrainEntity(
//                        passenger,
//                        points.get(0).getArrivalTime(),
//                        points.get(points.size()-1).getDepartureTime(),
//                        train);
//        if (countTickets>0){
//            return true;
//        }
        for (SeatEntity st : seats) {
            for (TicketEntity tk : st.getTickets()) {
                if (tk.getPassengerEntity().equals(passenger)
                        &&points.get(0).getDepartureTime().isBefore(tk.getPointOfSchedules().get(tk.getPointOfSchedules().size()-1).getArrivalTime())
                        &&points.get(points.size()-1).getArrivalTime().isAfter(tk.getPointOfSchedules().get(0).getDepartureTime())){
                    return true;
                }
            }
        }
        return false;
    }

}
