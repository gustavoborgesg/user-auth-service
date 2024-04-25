package com.example.auth.domain.exceptions;

import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PersonException {

    public CustomExceptionRequest exceptionNonExistentPerson() {
        throw new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_PERSON, "", HttpStatus.NOT_FOUND, false);
    }

    public void exceptionEmptyCpf() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_CPF, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionInvalidCpf() {
        throw new CustomExceptionRequest(ExceptionMessages.INVALID_CPF, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionDuplicatedCpf() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_CPF, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionEmptyEmail() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_EMAIL, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionInvalidEmail() {
        throw new CustomExceptionRequest(ExceptionMessages.INVALID_EMAIL, "", HttpStatus.BAD_REQUEST, false);
    }

    public void exceptionDuplicatedEmail() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_EMAIL, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionDatabaseError() {
        throw new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false);
    }
}
