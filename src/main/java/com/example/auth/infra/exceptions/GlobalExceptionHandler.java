package com.example.auth.infra.exceptions;

import com.example.auth.domain.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomExceptionRequest.class})
    public ResponseEntity<Object> handleCustomException(CustomExceptionRequest customExceptionRequest) {
        String message = customExceptionRequest.getMessage();
        String generalMessage = customExceptionRequest.getGeneralMessage();
        HttpStatus httpStatus = customExceptionRequest.getHttpStatus();
        LocalDateTime timeStamp = Utils.getDateTimeNowFormatted();
        Throwable cause = customExceptionRequest.getCause();

        StackTraceElement[] stackTraceElement = new StackTraceElement[0];
        if (customExceptionRequest.isShowStackTrace()) {
            stackTraceElement = customExceptionRequest.getStackTrace();
        }

        return new ResponseEntity<>(CustomException.builder()
                .message(message)
                .generalMessage(generalMessage)
                .httpStatus(httpStatus)
                .timeStamp(timeStamp)
                .cause(cause)
                .stackTraceElement(stackTraceElement)
                .build()
                , httpStatus);
    }

}