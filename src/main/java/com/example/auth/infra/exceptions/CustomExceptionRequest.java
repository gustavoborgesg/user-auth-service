package com.example.auth.infra.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class CustomExceptionRequest extends RuntimeException {
    private final String message;
    private final String generalMessage;
    private final HttpStatus httpStatus;
    private final boolean showStackTrace;

    public CustomExceptionRequest(String message, String generalMessage, HttpStatus httpStatus, boolean showStackTrace) {
        this.message = message;
        this.generalMessage = generalMessage;
        this.httpStatus = httpStatus;
        this.showStackTrace = showStackTrace;
    }
}
