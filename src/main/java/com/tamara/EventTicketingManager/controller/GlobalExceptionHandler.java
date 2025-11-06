package com.tamara.EventTicketingManager.controller;

import com.tamara.EventTicketingManager.domain.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.Binding;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){

        log.error("Caught MethodArgumentNotValidException", ex);

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream().findFirst().map(fieldError -> fieldError.getField() + ": "+ fieldError.getDefaultMessage()).orElse("Validation Error occured");

        ErrorDto errorDto = ErrorDto.builder()
                .error(errorMessage)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation (ConstraintViolationException ex){

        log.error("Caught ConstraintViolationException", ex);

        String errorMessage = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + ": " + violation.getMessage())
                .orElse("Constraint Violation Occured");



        ErrorDto errorDto = ErrorDto.builder()
                .error(errorMessage)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught Exception", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .error("An unknown error occcured")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
