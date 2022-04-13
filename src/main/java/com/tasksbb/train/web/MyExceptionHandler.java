package com.tasksbb.train.web;

import com.tasksbb.train.ex.*;
import com.tasksbb.train.validations.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> validationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TrainExistException.class)
    public ResponseEntity<ResponseError> trainExistException(TrainExistException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ResponseError> ScheduleNotFoundException(ScheduleNotFoundException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<ResponseError> SeatNotFoundException(SeatNotFoundException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StationExistException.class)
    public ResponseEntity<ResponseError> StationExistException(StationExistException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ResponseError> StationNotFoundException(StationNotFoundException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<ResponseError> TrainNotFoundException(TrainNotFoundException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ResponseError> UserExistException(UserExistException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PassengerInTrainException.class)
    public ResponseEntity<ResponseError> PassengerInTrainException(PassengerInTrainException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(StationIsOblivionException.class)
    public ResponseEntity<ResponseError> StationIsOblivionException(StationIsOblivionException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseError {
        private String message;
    }
}

