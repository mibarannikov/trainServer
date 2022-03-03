package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerEntityRepository extends JpaRepository<PassengerEntity, Long> {
}
