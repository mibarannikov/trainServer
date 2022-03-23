package com.tasksbb.train.service;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.ex.StationExistException;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.repository.StationEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {
    public static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    public final StationEntityRepository stationEntityRepository;


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
                station.getCanGetStations().add(stationEntityRepository.findByNameStation(s)
                        .orElseThrow(()-> new StationNotFoundException("Station with name "+s+" not found")));
            }
            station.getCanGetStations().forEach(st -> st.getCanGetStations().add(station));
            try {
                return stationEntityRepository.save(station);
            }catch (Exception ex){
                throw new StationExistException("station with name "+stationDto.getNameStation()+ "already exist");
            }

        }
        station.setCanGetStations(new LinkedHashSet<>());

        try {
            return stationEntityRepository.save(station);
        } catch (Exception ex){
            throw new StationExistException("station with name "+stationDto.getNameStation()+ "already exist");
        }
    }

    public StationDto findByNameStation(String name) {
        StationEntity station = stationEntityRepository.findByNameStation(name)
                .orElseThrow(()-> new StationNotFoundException("Station with name "+name+" not found"));
        return StationFacade.stationToStationDto(station);

    }
}
