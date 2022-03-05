package com.tasksbb.train.service;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.repository.StationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    public final StationEntityRepository stationEntityRepository;

    public StationService(StationEntityRepository stationEntityRepository) {
        this.stationEntityRepository = stationEntityRepository;
    }

    public StationEntity updateStation() {
        return null;
    }

    public StationEntity addStation(StationDto stationDto) {
        StationEntity station = new StationEntity();
        station.setNameStation(stationDto.getNameStation());
        station.setLatitude(stationDto.getLatitude());
        station.setLongitude(stationDto.getLongitude());
        if ((stationDto.getCanGetStation().length != 0)&&(stationDto.getCanGetStation()!=null)) {
            for (String s : stationDto.getCanGetStation()) {
                station.getCanGetStations().add(stationEntityRepository.findByNameStation(s)
                        .orElseThrow(() -> new StationNotFoundException("station with name " + s + " not found")));
            }
            station.getCanGetStations()
                    .stream()
                    .map(st -> st.getCanGetStations().add(station))
                    .collect(Collectors.toSet());
            return stationEntityRepository.save(station);
        }
        station.setCanGetStations(new LinkedHashSet<>());
        return stationEntityRepository.save(station);

    }
}
