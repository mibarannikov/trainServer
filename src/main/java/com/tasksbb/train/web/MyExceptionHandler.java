package com.tasksbb.train.web;

import com.tasksbb.train.validations.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

//@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> validationException(ValidationException ex){
        return new ResponseEntity<>(ex.getErrors(),HttpStatus.BAD_REQUEST);
    }

}
