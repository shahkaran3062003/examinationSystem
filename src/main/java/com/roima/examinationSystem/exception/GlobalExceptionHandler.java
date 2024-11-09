package com.roima.examinationSystem.exception;


import com.roima.examinationSystem.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        // Combine error messages into a single string, or return as list if you prefer
        String combinedErrorMessage = String.join(", ", errorMessages);
        ApiResponse apiResponse = new ApiResponse("Error", combinedErrorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingRequestParaException(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", "Missing Request Parameter"));

    }
}

