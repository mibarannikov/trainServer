package com.tasksbb.train.repository;

import com.tasksbb.train.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByUser(User user);

    List<TicketEntity> findAllByPointOfSchedules(PointOfScheduleEntity point);

    List<TicketEntity> findBySeatEntity_TrainEntity_TrainNumber(Long trainNumber);




}
