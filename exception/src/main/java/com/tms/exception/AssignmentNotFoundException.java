package com.tms.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String message){
        super(message);
    }
}
