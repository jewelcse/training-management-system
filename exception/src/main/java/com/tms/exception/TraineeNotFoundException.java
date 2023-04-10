package com.tms.exception;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException(String message){
        super(message);
    }
}
