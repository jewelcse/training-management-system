package com.tms.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> userNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AssignmentNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> assignmentNotFoundException(AssignmentNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BatchNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> batchNotFoundException(BatchNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CourseNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> courseNotFoundException(CourseNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> roleNotFoundException(RoleNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TraineeNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> traineeNotFoundException(TraineeNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TrainerNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> trainerNotFoundException(TrainerNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TraineesAssignmentSubmissionNotFoundException.class)
    public ResponseEntity<JsonExceptionResponse> traineesAssignmentSubmissionsNotFoundException(TraineesAssignmentSubmissionNotFoundException exception) {
        return new ResponseEntity<>(new JsonExceptionResponse(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<JsonExceptionResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new JsonExceptionResponse(exc.getMessage(),500));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class JsonExceptionResponse{
        private String message;
        private int status_code;
    }
}
