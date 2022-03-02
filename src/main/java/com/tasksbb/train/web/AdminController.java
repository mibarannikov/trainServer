package com.tasksbb.train.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {
    @GetMapping("/go")
    public ResponseEntity<String> getGoTest() {

        return new ResponseEntity<>("GO", HttpStatus.OK);
    }
}
