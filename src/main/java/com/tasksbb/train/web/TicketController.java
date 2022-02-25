package com.tasksbb.train.web;

import com.tasksbb.train.dto.TrainDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tiket")
@CrossOrigin
@PreAuthorize("permitAll()")
public class TicketController {


}
