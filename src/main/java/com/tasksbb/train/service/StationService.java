package com.tasksbb.train.service;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.repository.StationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class StationService {

    public final StationFacade stationFacade;
    public final StationEntityRepository stationEntityRepository;

    public StationService(StationFacade stationFacade,
                          StationEntityRepository stationEntityRepository) {
        this.stationFacade = stationFacade;
        this.stationEntityRepository = stationEntityRepository;
    }

    public StationEntity updateStation() {
        return null;
    }

    public List<StationEntity> findAllStation() {

        return stationEntityRepository.findByOrderByNameStationAsc();
    }

    public StationEntity addStation(StationDto stationDto) {
        StationEntity station = new StationEntity();
        station.setNameStation(stationDto.getNameStation());
        station.setLatitude(stationDto.getLatitude());
        station.setLongitude(stationDto.getLongitude());
        if ((stationDto.getCanGetStation().size() != 0) && (stationDto.getCanGetStation() != null)) {
            for (String s : stationDto.getCanGetStation()) {
                station.getCanGetStations().add(stationEntityRepository.findByNameStation(s));
            }
            station.getCanGetStations().forEach(st -> st.getCanGetStations().add(station));
            return stationEntityRepository.save(station);
        }
        station.setCanGetStations(new LinkedHashSet<>());
        return stationEntityRepository.save(station);

    }

    public StationDto findByNameStation(String name) {
        StationEntity station = stationEntityRepository.findByNameStation(name);
        return stationFacade.stationToStationDto(station);

    }
}
