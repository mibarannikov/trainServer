package com.tasksbb.train.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TrainNotFoundException extends RuntimeException {
    public TrainNotFoundException(String msg) {
        super(msg);
    }
}
