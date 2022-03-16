package com.tasksbb.train.repository;

import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainEntityRepository extends JpaRepository<TrainEntity, Long> {

    Optional<TrainEntity> findByTrainNumber(Long trainNumber);
    List<TrainEntity> findAllByOrderByDepartureTimeAsc();



}
