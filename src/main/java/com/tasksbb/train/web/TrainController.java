package com.tasksbb.train.web;

import com.tasksbb.train.dto.TrainDto;
import com.tasksbb.train.dto.TransferDto;
import com.tasksbb.train.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/train")
@CrossOrigin
@PreAuthorize("permitAll()")
public class TrainController {


    private final TrainService trainService;


    @GetMapping("/search")
    public ResponseEntity<List<TrainDto>> trains(@RequestParam(name = "start") String startStationName,
                                                 @RequestParam(name = "end") String endStationName,
                                                 @RequestParam(name = "tpstart") String startTimePeriod,
                                                 @RequestParam(name = "tpend") String endTimePeriod) {
        LocalDateTime dateTimeStart = LocalDateTime.parse(startTimePeriod);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endTimePeriod);
        List<TrainDto> trains = trainService.findAllStartEndTimePeriod(startStationName,endStationName,dateTimeStart,dateTimeEnd);

        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/searchtransfer")
    public ResponseEntity<List<TransferDto>> trainsWithTransfer(@RequestParam(name = "start") String startStationName,
                                                 @RequestParam(name = "end") String endStationName,
                                                 @RequestParam(name = "tpstart") String startTimePeriod,
                                                 @RequestParam(name = "tpend") String endTimePeriod) {
        LocalDateTime dateTimeStart = LocalDateTime.parse(startTimePeriod);
       LocalDateTime dateTimeEnd = LocalDateTime.parse(endTimePeriod);
        List<TransferDto> transfers = trainService.findAllStartEndTimePeriodTransfer(startStationName,endStationName,dateTimeStart,dateTimeEnd);

        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

//    @GetMapping("/freeseats")
//    public
//    ResponseEntity<List<SeatEntityDto>> getFreeSeats(@RequestParam(name = "train") Long trainNumber,
//                                                     @RequestParam(name = "start") String startStation,
//                                                     @RequestParam(name = "end") String endStation){
//        List<SeatEntityDto> seats = new ArrayList<>();
//      //  seats = trainService.findAllFreeSeats(trainNumber,startStation,endStation);
//        return new ResponseEntity<>(seats, HttpStatus.OK);
//    }




}
