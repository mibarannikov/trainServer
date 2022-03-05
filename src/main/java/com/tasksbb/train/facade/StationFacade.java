package com.tasksbb.train.facade;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import org.springframework.stereotype.Component;

@Component
public class StationFacade {
    public StationDto stationToStationDto(StationEntity station){
        StationDto stationDto = new StationDto();
        stationDto.setId(station.getId());
        stationDto.setNameStation(station.getNameStation());
        stationDto.setLatitude(station.getLatitude());
        stationDto.setLongitude(station.getLongitude());

        String[] arr = new String[station.getCanGetStations().size()];
        int i=0;
        for (StationEntity st: station.getCanGetStations()) {
            arr[i] = st.getNameStation();
            i++;
        }
        stationDto.setCanGetStation(arr);
        return stationDto;


    }
}
