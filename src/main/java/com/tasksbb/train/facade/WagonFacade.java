package com.tasksbb.train.facade;

import com.tasksbb.train.dto.WagonDto;
import com.tasksbb.train.entity.WagonEntity;

public class WagonFacade {
    public static WagonDto wagonToWagonDto(WagonEntity wagon) {
        WagonDto wagonDto = new WagonDto();
        wagonDto.setId(wagon.getId());
        wagonDto.setWagonNumber(wagon.getWagonNumber());
        wagonDto.setType(wagon.getType());
        wagonDto.setName(wagon.getNameWagon());
        wagonDto.setSumSeats(wagon.getSumSeats());
        wagonDto.setTrainNumber(wagonDto.getTrainNumber());
        return wagonDto;
    }

}
