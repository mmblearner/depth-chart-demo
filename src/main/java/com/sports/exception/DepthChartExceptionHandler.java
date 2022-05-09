package com.sports.exception;

import com.sports.payload.response.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class DepthChartExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Error> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        if (null == dataNotFoundException.getError()) {
            Error error = Error.builder().errorCode(dataNotFoundException.getMessage()).summary(dataNotFoundException.getCause().getMessage()).description(dataNotFoundException.getMessage()).build();
            dataNotFoundException.setError(error);
        }
        return new ResponseEntity<Error>(dataNotFoundException.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidServiceRequestException.class)
    public ResponseEntity<Error> handleInvalidServiceException(InvalidServiceRequestException invalidServiceRequestException) {
        if (null == invalidServiceRequestException.getError()) {
            Error error = Error.builder().errorCode(invalidServiceRequestException.getMessage()).summary(invalidServiceRequestException.getCause().getMessage()).description(invalidServiceRequestException.getMessage()).build();
            invalidServiceRequestException.setError(error);
        }
        return new ResponseEntity<Error>(invalidServiceRequestException.getError(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessages = ex.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));
        Error error = Error.builder().errorCode("ER-1001").summary("Bad Input Request").description(errorMessages).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
