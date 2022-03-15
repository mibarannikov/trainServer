package com.tasksbb.train.repository;

import com.tasksbb.train.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByUser(User user);


    TicketEntity findAllBySeatEntityAndPassengerEntity(SeatEntity seat,PassengerEntity passenger);
    //@Query(  )
    List<TicketEntity> findAllByPointOfSchedules(PointOfScheduleEntity point);



}
