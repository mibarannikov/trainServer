package com.tasksbb.train.facade;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import org.springframework.stereotype.Component;



public class StationFacade {
    public static StationDto stationToStationDto(StationEntity station) {
        StationDto stationDto = new StationDto();
        stationDto.setId(station.getId());
        stationDto.setNameStation(station.getNameStation());
        stationDto.setLatitude(station.getLatitude());
        stationDto.setLongitude(station.getLongitude());
        station.getCanGetStations().forEach(st ->
                stationDto.getCanGetStation().add(st.getNameStation()));
        return stationDto;


    }
}
