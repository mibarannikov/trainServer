package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PointOfScheduleRepository extends JpaRepository<PointOfScheduleEntity, Long> {

    List<PointOfScheduleEntity> findByTrainEntity(TrainEntity train);

    Optional<PointOfScheduleEntity> findByStationEntityNameStation(String name);

    List<PointOfScheduleEntity> findPointOfScheduleEntityByTrainEntityId(Long id);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationOrderByArrivalTimeAsc(String name);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationAndArrivalTimeAfterAndArrivalTimeBeforeOrderByArrivalTimeAsc(String name, LocalDateTime after, LocalDateTime before);
    PointOfScheduleEntity findByTrainEntityAndStationEntityNameStation(TrainEntity train, String nameStation);//todo Optional?
    List<PointOfScheduleEntity> findAllByTrainEntityOrderByArrivalTimeAsc(TrainEntity train);

}
