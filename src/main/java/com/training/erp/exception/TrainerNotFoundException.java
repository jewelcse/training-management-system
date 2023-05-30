package com.training.erp.exception;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message){
        super(message);
    }
}
