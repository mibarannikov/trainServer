package com.tasksbb.train.web;

import com.tasksbb.train.dto.TrainDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/train")
@CrossOrigin //todo
@PreAuthorize("permitAll()")
public class TrainController {

    private List<TrainDto> trains;

    @GetMapping("/{start}/{end}/")
    public ResponseEntity<List<TrainDto>> trains(@PathVariable("start") String startStation,
                                                 @PathVariable("end") String stopStation) {
        trains = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TrainDto train = new TrainDto();
            train.setId(i);
            train.setTrainNumber(2323 + i * 3);
            train.setSeatCapacity(34);
            train.setStart(LocalDateTime.of(2022, 2, 23, i, i * 3, 0));
            train.setStop(LocalDateTime.of(2022, 2, 23, i + 1, 0, 0));
            train.setStartStation(startStation);
            train.setStopStation(stopStation);
            trains.add(train);
        }
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TrainDto> trainById(@PathVariable("id") int id) {

        return new ResponseEntity<>(trains.get(id), HttpStatus.OK);
    }

}
