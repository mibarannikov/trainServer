package com.tasksbb.train.repository;

import com.tasksbb.train.entity.SeatEntity;
import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatEntityRepository extends JpaRepository<SeatEntity, Long> {

    SeatEntity findByTrainEntityTrainNumberAndSeatNumber(Long trainNumber, Long SeatNumber);// todo optional?

    List<SeatEntity> findByTrainEntity(TrainEntity train);
    

}
