package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerEntityRepository extends JpaRepository<PassengerEntity, Long> {

}
