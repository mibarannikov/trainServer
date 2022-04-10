package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByUser(User user);

    List<TicketEntity> findAllByPointOfSchedules(PointOfScheduleEntity point);

    List<TicketEntity> findBySeatEntity_TrainEntity_TrainNumber(Long trainNumber);


}
