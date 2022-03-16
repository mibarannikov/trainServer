package com.tasksbb.train.facade;

import com.tasksbb.train.dto.PointOfScheduleDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.repository.StationEntityRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PointOfScheduleFacade {

//    public final StationEntityRepository stationEntityRepository;

    public PointOfScheduleFacade() {
//        this.stationEntityRepository = stationEntityRepository;
    }

//    public PointOfScheduleEntity dtoToPointOfScheduleEntity(LocalDateTime time, String nameStation) {
//
//        PointOfScheduleEntity pointOfSchedule = new PointOfScheduleEntity();
//        pointOfSchedule.setArrivalTime(time);
//        pointOfSchedule.setStationEntity(stationEntityRepository.findByNameStation(nameStation));
//        return pointOfSchedule;
//    }

    public static PointOfScheduleDto pointEntityToPointDto(PointOfScheduleEntity point) {
        PointOfScheduleDto pointDto = new PointOfScheduleDto();
        pointDto.setId(point.getId());
        pointDto.setNameStation(point.getStationEntity().getNameStation());
        pointDto.setArrivalTime(point.getArrivalTime());
        pointDto.setDepartureTime(point.getDepartureTime());
        return pointDto;
    }
}
