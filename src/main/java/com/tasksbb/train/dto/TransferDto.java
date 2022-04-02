package com.tasksbb.train.dto;

import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {
    private TrainDto firstTrain;
    private TrainDto secondTrain;
    private StationDto stationTransfer;
}
