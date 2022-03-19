package com.tasksbb.train.repository;

import com.tasksbb.train.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByUser(User user);


    TicketEntity findAllBySeatEntityAndPassengerEntity(SeatEntity seat,PassengerEntity passenger);

    List<TicketEntity> findAllByPointOfSchedules(PointOfScheduleEntity point);

    List<TicketEntity> findByPassengerEntityAndPointOfSchedules_DepartureTimeBeforeAndPointOfSchedules_ArrivalTimeAndPointOfSchedules_TrainEntity(PassengerEntity passengerEntity, LocalDateTime departureTime, LocalDateTime arrivalTime, TrainEntity trainEntity);

    long countByPassengerEntityAndPointOfSchedules_DepartureTimeAfterAndPointOfSchedules_ArrivalTimeBeforeAndPointOfSchedules_TrainEntity(PassengerEntity passengerEntity, LocalDateTime arrivalTime, LocalDateTime departureTime, TrainEntity trainEntity);

    List<TicketEntity> findBySeatEntity_TrainEntity(TrainEntity trainEntity);

    List<TicketEntity> findBySeatEntity_TrainEntity_TrainNumber(Long trainNumber);




}
