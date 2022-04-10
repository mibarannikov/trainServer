package com.tasksbb.train.repository;

import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatEntityRepository extends JpaRepository<SeatEntity, Long> {

    Optional<SeatEntity> findByTrainEntityTrainNumberAndSeatNumber(Long trainNumber, Long SeatNumber);

    List<SeatEntity> findByTrainEntity(TrainEntity train);


}
