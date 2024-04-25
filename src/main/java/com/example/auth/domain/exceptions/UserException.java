package com.example.auth.domain.exceptions;

import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserException {

    public CustomExceptionRequest exceptionNonExistentUser() {
        throw new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_USER, "", HttpStatus.NOT_FOUND, false);
    }

    public void exceptionDisabledUser() {
        throw new CustomExceptionRequest(ExceptionMessages.DISABLED_USER, "", HttpStatus.FORBIDDEN, false);
    }

    public void exceptionDuplicatedPerson() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_PERSON, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionDatabaseError() {
        throw new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false);
    }
}