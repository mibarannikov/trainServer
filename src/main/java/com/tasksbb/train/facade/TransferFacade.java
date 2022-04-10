package com.tasksbb.train.facade;

import com.tasksbb.train.dto.TransferDto;
import com.tasksbb.train.service.Transfer;

public class TransferFacade {
    public static TransferDto transferToTransferDto(Transfer transfer) {
        TransferDto transferDto = new TransferDto();
        transferDto.setFirstTrain(TrainFacade.trainToDto(transfer.getFirstTrain()));
        transferDto.setSecondTrain(TrainFacade.trainToDto(transfer.getSecondTrain()));
        transferDto.setStationTransfer(StationFacade.stationToStationDto(transfer.getStationTransfer()));
        return transferDto;
    }
}
