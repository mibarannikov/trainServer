package com.tasksbb.train.repository;

import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainEntityRepository extends JpaRepository<TrainEntity, Long> {

    Optional<TrainEntity> findByTrainNumber(Long trainNumber);

    Page<TrainEntity> findAllByOrderByDepartureTimeDesc(Pageable page);

    Page<TrainEntity> findByArrivalTimeEndBeforeOrderByDepartureTimeDesc(LocalDateTime arrivalTimeEnd,Pageable  page);

   Page<TrainEntity> findByArrivalTimeEndAfter(LocalDateTime arrivalTimeEnd,Pageable page);



    List<TrainEntity> findByPointOfSchedules_StationEntity_Id(Long id);




}
