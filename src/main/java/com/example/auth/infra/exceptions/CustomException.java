package com.example.auth.infra.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class CustomException {
    private final String message;
    private final String generalMessage;
    private final HttpStatus httpStatus;
    private final LocalDateTime timeStamp;
    private final Throwable cause;
    private final StackTraceElement[] stackTraceElement;
}
