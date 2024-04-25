package com.example.auth.domain.exceptions.auth;

import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthException {

    public void exceptionEmptyToken() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_TOKEN, "", HttpStatus.BAD_REQUEST, false);
    }

    public CustomExceptionRequest exceptionInvalidUsername() {
        throw new CustomExceptionRequest(ExceptionMessages.INVALID_AUTHENTICATION, "", HttpStatus.NOT_FOUND, false);
    }

    public void exceptionDuplicatedIdOldUser() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_IDOLDUSER, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionEmptyUsername() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_USERNAME, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionDuplicatedUsername() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_USERNAME, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionEmptyPassword() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_PASSWORD, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionInvalidPassword() {
        throw new CustomExceptionRequest(ExceptionMessages.INVALID_AUTHENTICATION, "", HttpStatus.UNAUTHORIZED, false);
    }

    public void exceptionDatabaseError() {
        throw new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false);
    }
}
