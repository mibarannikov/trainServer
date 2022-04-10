package com.tasksbb.train.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MyExpiredJwtException extends RuntimeException {

    public MyExpiredJwtException(String msg) {
        super(msg);
    }

}
