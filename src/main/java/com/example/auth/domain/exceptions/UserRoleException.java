package com.example.auth.domain.exceptions;

import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserRoleException {

    public void exceptionEmptyUserRoleName() {
        throw new CustomExceptionRequest(ExceptionMessages.EMPTY_ROLE, "", HttpStatus.BAD_REQUEST, false);
    }

    public CustomExceptionRequest exceptionNonExistentUserRole() {
        throw new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_ROLE, "", HttpStatus.NOT_FOUND, false);
    }

    public void exceptionDuplicatedUserRoleName() {
        throw new CustomExceptionRequest(ExceptionMessages.DUPLICATED_USER_ROLE, "", HttpStatus.CONFLICT, false);
    }

    public void exceptionDatabaseError() {
        throw new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false);
    }
}
