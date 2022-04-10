package com.tasksbb.train.facade;

import com.tasksbb.train.dto.PointOfScheduleDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;


public class PointOfScheduleFacade {

    public static PointOfScheduleDto pointEntityToPointDto(PointOfScheduleEntity point) {
        PointOfScheduleDto pointDto = new PointOfScheduleDto();
        pointDto.setId(point.getId());
        pointDto.setNameStation(point.getStationEntity().getNameStation());
        pointDto.setArrivalTime(point.getArrivalTime());
        pointDto.setArrivalTimeInit(point.getArrivalTimeInit());
        pointDto.setDepartureTime(point.getDepartureTime());
        pointDto.setDepartureTimeInit(point.getDepartureTimeInit());
        pointDto.setDelayed(point.getDelayed());
        return pointDto;
    }
}
