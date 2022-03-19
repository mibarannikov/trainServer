package com.tasksbb.train.repository;

import com.tasksbb.train.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationEntityRepository extends JpaRepository<StationEntity, Long> {

    Optional<StationEntity> findByNameStation(String nameStation);


    List<StationEntity> findByOrderByNameStationAsc();

}
