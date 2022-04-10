package com.tasksbb.train.facade;

import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.entity.TrainEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

public class TrainFacade {
    public static TrainDto trainToDto(TrainEntity train) {
        TrainDto trainDto = new TrainDto();
        trainDto.setId(train.getId());
        trainDto.setTrainNumber(train.getTrainNumber());
        trainDto.setTrainSpeed(train.getTrainSpeed());
        trainDto.setDepartureTime(train.getDepartureTime());
        trainDto.setArrivalTimeEnd(train.getArrivalTimeEnd());
        trainDto.setSumSeats((long) train.getSeatEntities().size());
        trainDto.setPointsOfSchedule(train.getPointOfSchedules().stream().map(PointOfScheduleFacade::pointEntityToPointDto).collect(Collectors.toList()));
        trainDto.setWagons(train.getWagonEntities().stream().map(WagonFacade::wagonToWagonDto).collect(Collectors.toList()));
        trainDto.setAmountOfEmptySeats(train.getAmountOfEmptySeats());
        return trainDto;
    }

}
