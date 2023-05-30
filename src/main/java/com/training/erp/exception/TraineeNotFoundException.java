package com.training.erp.exception;

public class TraineeNotFoundException extends RuntimeException {
    public TraineeNotFoundException(String message){
        super(message);
    }
}
