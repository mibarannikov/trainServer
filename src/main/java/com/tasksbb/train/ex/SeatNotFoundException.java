package com.tasksbb.train.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String msg) {
        super(msg);

    }

}
