package com.tasksbb.train.service;

import com.tasksbb.train.entity.*;
import com.tasksbb.train.repository.PassengerEntityRepository;
import com.tasksbb.train.repository.SeatEntityRepository;
import com.tasksbb.train.repository.TicketEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    public final TicketEntityRepository ticketEntityRepository;
    public final PassengerEntityRepository passengerEntityRepository;
    public final TrainEntityRepository trainEntityRepository;
    public final SeatEntityRepository seatEntityRepository;

    public Boolean passengerIsPresent(TrainEntity train, PassengerEntity passenger, List<PointOfScheduleEntity> points) {
        // List<SeatEntity> seats = seatEntityRepository.findByTrainEntity(train);// todo заменить на билеты поезда и избавиться от одно фора
         List<TicketEntity> tickets = ticketEntityRepository.findBySeatEntity_TrainEntity_TrainNumber(train.getTrainNumber());
//        long countTickets = ticketEntityRepository
//                .countByPassengerEntityAndPointOfSchedules_DepartureTimeAfterAndPointOfSchedules_ArrivalTimeBeforeAndPointOfSchedules_TrainEntity(
//                        passenger,
//                        points.get(0).getArrivalTime(),
//                        points.get(points.size()-1).getDepartureTime(),
//                        train);
//        if (countTickets>0){
//            return true;
//        }
       // for (SeatEntity st : seats) {
            for (TicketEntity tk : tickets) {
                if (tk.getPassengerEntity().equals(passenger)
                        &&points.get(0).getDepartureTime().isBefore(tk.getPointOfSchedules().get(tk.getPointOfSchedules().size()-1).getArrivalTime())
                        &&points.get(points.size()-1).getArrivalTime().isAfter(tk.getPointOfSchedules().get(0).getDepartureTime())){
                    return true;
                }
            }
       // }
        return false;
    }

}
