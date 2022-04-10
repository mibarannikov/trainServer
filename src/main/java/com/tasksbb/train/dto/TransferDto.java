package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {
    private TrainDto firstTrain;
    private TrainDto secondTrain;
    private StationDto stationTransfer;
    // private LocalDateTime wait;
}
