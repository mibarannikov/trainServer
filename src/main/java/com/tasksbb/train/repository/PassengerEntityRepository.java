package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PassengerEntityRepository extends JpaRepository<PassengerEntity, Long> {
    Optional<PassengerEntity> findByFirstnameAndLastnameAndDateOfBirth(String firstname, String lastname, LocalDate date);

}
