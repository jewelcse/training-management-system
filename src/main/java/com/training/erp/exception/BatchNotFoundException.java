package com.training.erp.exception;

public class BatchNotFoundException extends RuntimeException {
    public BatchNotFoundException(String message){
        super(message);
    }
}
