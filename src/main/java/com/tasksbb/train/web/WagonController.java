package com.tasksbb.train.web;

import com.tasksbb.train.dto.WagonDto;
import com.tasksbb.train.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/wagon")
@CrossOrigin
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class WagonController {

    private final TrainService trainService;

    @GetMapping("/all")
    public ResponseEntity<List<WagonDto>> getAllWagon(){

        return new ResponseEntity<>(trainService.findAllWagon(), HttpStatus.OK);

    }
}
