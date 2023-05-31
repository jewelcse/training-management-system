package com.training.erp.exception;

public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String message){
        super(message);
    }
}
