package com.tasksbb.train.repository;

import com.tasksbb.train.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationEntityRepository extends JpaRepository<StationEntity, Long> {
}
