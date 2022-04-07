package com.tasksbb.train.service;

import com.tasksbb.train.entity.StationEntity;
import com.tasksbb.train.entity.TrainEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transfer {
    private TrainEntity firstTrain;
    private TrainEntity secondTrain;
    private StationEntity stationTransfer;
//    private LocalDateTime wait;

    public Transfer(TrainEntity firstTrain, TrainEntity secondTrain, StationEntity stationTransfer) {  //, LocalDateTime wait
        this.firstTrain = firstTrain;
        this.secondTrain = secondTrain;
        this.stationTransfer = stationTransfer;
        //this.wait = wait;
    }
}
