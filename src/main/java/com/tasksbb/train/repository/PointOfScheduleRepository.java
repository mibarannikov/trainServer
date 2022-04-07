package com.tasksbb.train.repository;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PointOfScheduleRepository extends JpaRepository<PointOfScheduleEntity, Long> {

    List<PointOfScheduleEntity> findByStationEntity_IdAndDepartureTimeAfter(Long id, LocalDateTime departureTime);

    List<PointOfScheduleEntity> findByStationEntity_NameStationAndArrivalTimeAfterOrderByArrivalTimeAsc(String nameStation, LocalDateTime arrivalTime);

    Optional<PointOfScheduleEntity> findByTrainEntityAndStationEntityNameStation(TrainEntity train, String nameStation);
    List<PointOfScheduleEntity> findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(Long trainNumber, LocalDateTime now);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(String nameStation, LocalDateTime now);
    List<PointOfScheduleEntity> findAllByArrivalTimeAfterOrderByArrivalTime(LocalDateTime now);
    List<PointOfScheduleEntity> findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(String nameStation, LocalDateTime departureTime, LocalDateTime departureTime1);

}
