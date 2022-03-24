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

    Optional<PointOfScheduleEntity> findByStationEntityId(String name);

    List<PointOfScheduleEntity> findByStationEntity_Id(Long id);

    List<PointOfScheduleEntity> findByStationEntity_IdAndDepartureTimeAfter(Long id, LocalDateTime departureTime);


    List<PointOfScheduleEntity> findPointOfScheduleEntityByTrainEntityId(Long id);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationOrderByArrivalTimeAsc(String name);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationAndArrivalTimeAfterAndArrivalTimeBeforeOrderByArrivalTimeAsc(String name, LocalDateTime after, LocalDateTime before);
    Optional<PointOfScheduleEntity> findByTrainEntityAndStationEntityNameStation(TrainEntity train, String nameStation);//todo Optional?
    List<PointOfScheduleEntity> findAllByTrainEntityOrderByArrivalTimeAsc(TrainEntity train);
    List<PointOfScheduleEntity> findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(Long trainNumber, LocalDateTime now);
    Optional<PointOfScheduleEntity> findFirstByTrainEntityTrainNumberAndArrivalTimeBeforeOrderByArrivalTimeDesc(Long trainNumber, LocalDateTime now);
    List<PointOfScheduleEntity> findAllByStationEntityNameStationAndArrivalTimeAfterOrderByArrivalTime(String nameStation, LocalDateTime now);

    List<PointOfScheduleEntity> findByStationEntity_NameStationAndDepartureTimeAfterAndDepartureTimeBeforeOrderByDepartureTimeAsc(String nameStation, LocalDateTime departureTime, LocalDateTime departureTime1);

}
