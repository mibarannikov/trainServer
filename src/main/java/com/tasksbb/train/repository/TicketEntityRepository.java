package com.tasksbb.train.repository;

import com.tasksbb.train.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByUser(User user);
    //List<TicketEntity> findAllBySeatEntityAndPointOfSchedules(SeatEntity seat, PointOfScheduleEntity point);
    //List<TicketEntity> findAllBySeatEntityAndPointOfSchedulesNotIn(List<PointOfScheduleEntity> points);

    TicketEntity findAllBySeatEntityAndPassengerEntity(SeatEntity seat,PassengerEntity passenger);


}
