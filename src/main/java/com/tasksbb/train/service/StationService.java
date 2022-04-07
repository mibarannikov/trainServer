package com.tasksbb.train.service;

import com.tasksbb.train.dto.StationDto;
import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.entity.enums.EStatus;
import com.tasksbb.train.ex.StationExistException;
import com.tasksbb.train.ex.StationNotFoundException;
import com.tasksbb.train.facade.StationFacade;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import com.tasksbb.train.repository.StationEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import com.tasksbb.train.repository.WagonEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StationService {
    public static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    private static final Double EARTH_RADIUS = 6372795.0;

    public final StationEntityRepository stationEntityRepository;

    public final PointOfScheduleRepository pointOfScheduleRepository;

    public final TrainEntityRepository trainEntityRepository;
    public final WagonEntityRepository wagonEntityRepository;


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
                        .orElseThrow(() -> new StationNotFoundException("Station with name " + s + " not found")));
            }
            station.getCanGetStations().forEach(st -> st.getCanGetStations().add(station));
            try {
                return stationEntityRepository.save(station);
            } catch (Exception ex) {
                throw new StationExistException("station with name " + stationDto.getNameStation() + "already exist");// todo переделать
            }
        }
        station.setCanGetStations(new LinkedHashSet<>());
        try {
            return stationEntityRepository.save(station);
        } catch (Exception ex) {
            throw new StationExistException("station with name " + stationDto.getNameStation() + "already exist");//todo переделать
        }
    }

    public StationDto findByNameStation(String name) {
        StationEntity station = stationEntityRepository.findByNameStation(name)
                .orElseThrow(() -> new StationNotFoundException("Station with name " + name + " not found"));
        return StationFacade.stationToStationDto(station);
    }

    public List<StationDto> findAllSearchStation(String value) {

        List<StationEntity> searchStation;
        if (value.equals("all")){
            searchStation = stationEntityRepository.findByOrderByNameStationAsc();
        } else{
            searchStation = stationEntityRepository.findByNameStationStartsWithOrderByNameStationAsc(value);
        }

        return searchStation.stream()
                .map(StationFacade::stationToStationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StationDto editStation(StationDto stationDto) {
        StationEntity station = stationEntityRepository.findById(stationDto.getId())
                .orElseThrow(() -> new StationNotFoundException("not found station with id " + stationDto.getId()));

        if ((!Objects.equals(station.getLatitude(), stationDto.getLatitude())) || (!Objects.equals(station.getLongitude(), stationDto.getLongitude()))) {
            updatePointOfSchedule(station, stationDto.getLatitude(), stationDto.getLongitude());
        }
        Set<StationEntity> newCanGet = new LinkedHashSet<>();
        for (String st : stationDto.getCanGetStation()) {
            StationEntity stationEntity = stationEntityRepository.findByNameStation(st)
                    .orElseThrow(() -> new StationNotFoundException("not found station with name " + st));
            newCanGet.add(stationEntity);
        }
        Set<StationEntity> result = new LinkedHashSet<>();
        result.clear();
        result.addAll(station.getCanGetStations());
        result.removeAll(newCanGet);
        if (result.size() > 0) {
            for (StationEntity s : result) {
                deleteStationFromCanGet(s, station);
                setStatusPointOfSchedule(s, station, EStatus.cancel);
            }
        }
        result.clear();
        result.addAll(newCanGet);
        result.removeAll(station.getCanGetStations());
        if (result.size() > 0) {
            for (StationEntity s : result) {
                addStationToCanGet(s, station);
                setStatusPointOfSchedule(s, station, EStatus.schedule);

            }
        }
        station.setCanGetStations(newCanGet);
        if (!station.getNameStation().equals(stationDto.getNameStation())) {
            station.setNameStation(stationDto.getNameStation());
        }

        StationDto stationDtoOut = StationFacade.stationToStationDto(stationEntityRepository.save(station));

        return stationDtoOut;
    }

    private void addStationToCanGet(StationEntity station, StationEntity addStation) {
        station.getCanGetStations().add(addStation);
        stationEntityRepository.save(station);
    }

    private void deleteStationFromCanGet(StationEntity station, StationEntity deleteStation) {
        Set<StationEntity> can = station.getCanGetStations();
        can.remove(deleteStation);
        station.setCanGetStations(can);
        stationEntityRepository.save(station);
    }


    private void setStatusPointOfSchedule(StationEntity station, StationEntity changeStation, EStatus status) {
        List<PointOfScheduleEntity> savePoints = new ArrayList<>();
        List<PointOfScheduleEntity> point1 = pointOfScheduleRepository.findByStationEntity_IdAndDepartureTimeAfter(station.getId(), LocalDateTime.now());
        List<PointOfScheduleEntity> point2 = pointOfScheduleRepository.findByStationEntity_IdAndDepartureTimeAfter(changeStation.getId(), LocalDateTime.now());

//        Predicate<PointOfScheduleEntity> predicateTrain = p-> point2.stream().anyMatch(p1->p.getTrainEntity().equals(p1.getTrainEntity()));
//        point1.stream().filter(point->{point2})

        for (PointOfScheduleEntity p1 : point1) {
            for (PointOfScheduleEntity p2 : point2) {
                if (p1.getTrainEntity().equals(p2.getTrainEntity())
                        && (Math.abs(p1.getTrainEntity().getPointOfSchedules().indexOf(p1) - p1.getTrainEntity().getPointOfSchedules().indexOf(p2)) == 1)) {
                    int idx = Math.max(p1.getTrainEntity().getPointOfSchedules().indexOf(p1), p1.getTrainEntity().getPointOfSchedules().indexOf(p2));
                    for (PointOfScheduleEntity point : p1.getTrainEntity().getPointOfSchedules()) {
                        if (p1.getTrainEntity().getPointOfSchedules().indexOf(point) >= idx) {
                            point.setDelayed(status);
                            savePoints.add(point);
                        }
                    }
                }
            }
        }

        pointOfScheduleRepository.saveAll(savePoints);
    }

    private void updatePointOfSchedule(StationEntity station, Double lat, Double lon) {
        List<TrainEntity> trains = trainEntityRepository.findByPointOfSchedules_StationEntity_Id(station.getId());
        boolean afterFindStation = false;

        for (TrainEntity tr : trains) {
            for (int i = 0; i < tr.getPointOfSchedules().size(); i++) {
                if (tr.getPointOfSchedules().get(i).getStationEntity().getId() == station.getId()) {
                    tr.getPointOfSchedules().get(i).getStationEntity().setLongitude(lon);
                    tr.getPointOfSchedules().get(i).getStationEntity().setLatitude(lat);
                    stationEntityRepository.save(tr.getPointOfSchedules().get(i).getStationEntity());
                    afterFindStation = true;
                }
                if ((i > 0) && afterFindStation) {

                    double newDistance = distanceCalculation(tr.getPointOfSchedules().get(i),tr.getPointOfSchedules().get(i - 1));

                    long sec = (long) Math.floor((newDistance / (tr.getTrainSpeed() * 0.2777)) - 10);
                    if (tr.getPointOfSchedules().get(i).getArrivalTime().isBefore(tr.getPointOfSchedules().get(i - 1).getDepartureTime().plusSeconds(sec))) {
                        long trainStopSecond = tr.getPointOfSchedules().get(i).getDepartureTime().toEpochSecond(ZoneOffset.of("+0")) -
                                tr.getPointOfSchedules().get(i).getArrivalTime().toEpochSecond(ZoneOffset.of("+0"));
                        tr.getPointOfSchedules().get(i).setArrivalTime(tr.getPointOfSchedules().get(i - 1).getDepartureTime().plusSeconds(sec));
                        tr.getPointOfSchedules().get(i).setDepartureTime(tr.getPointOfSchedules().get(i).getArrivalTime().plusSeconds(trainStopSecond));
                        tr.getPointOfSchedules().get(i).setDelayed(EStatus.running_with_errors);
                    } else {
                        tr.getPointOfSchedules().get(i).setDelayed(EStatus.schedule);
                    }
                    pointOfScheduleRepository.save(tr.getPointOfSchedules().get(i));
                }
            }
        }
    }



    public double distanceCalculation(PointOfScheduleEntity point1, PointOfScheduleEntity point2){
        double latRad = point1.getStationEntity().getLatitude() * Math.PI / 180;
        double lonRad = point1.getStationEntity().getLongitude() * Math.PI / 180;
        double latRadPre = point2.getStationEntity().getLatitude() * Math.PI / 180;
        double lonRadPre = point2.getStationEntity().getLongitude() * Math.PI / 180;
        double delta = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((latRad - latRadPre)) * 0.5, 2)
                + Math.cos(latRad) * Math.cos(latRadPre) * Math.pow(Math.sin((lonRad - lonRadPre) * 0.5), 2)));
        double Distance = delta * EARTH_RADIUS;
        return Distance;

    }
}
