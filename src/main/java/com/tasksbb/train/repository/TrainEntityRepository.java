package com.tasksbb.train.repository;

import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainEntityRepository extends JpaRepository<TrainEntity, Long> {
}
