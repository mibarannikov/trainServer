package com.tasksbb.train.repository;

import com.tasksbb.train.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {
}
