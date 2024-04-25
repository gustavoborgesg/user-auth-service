package com.example.auth.infra.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionMessages {
    public static final String ERROR_DATABASE_CONNECTION = "Unknown database connection error.";
    public static final String NONEXISTENT_USER = "User is non existent.";
    public static final String NONEXISTENT_ROLE = "User role is non existent.";
    public static final String NONEXISTENT_PERSON = "Person is non existent.";
    public static final String EMPTY_TOKEN = "Token is empty or null.";
    public static final String EMPTY_USERNAME = "Username is empty or null.";
    public static final String EMPTY_PASSWORD = "Password is empty or null.";
    public static final String EMPTY_CPF = "CPF is empty or null.";
    public static final String EMPTY_EMAIL = "E-mail is empty or null.";
    public static final String EMPTY_ROLE = "Role is empty or null.";
    public static final String INVALID_CPF = "Invalid CPF.";
    public static final String INVALID_AUTHENTICATION = "Invalid username or password.";
    public static final String INVALID_EMAIL = "Invalid e-mail.";
    public static final String DISABLED_USER = "User is disabled in the system.";
    public static final String DUPLICATED_IDOLDUSER = "User with this id for old user already exists.";
    public static final String DUPLICATED_USERNAME = "User with this username already exists.";
    public static final String DUPLICATED_CPF = "User with this CPF already exists.";
    public static final String DUPLICATED_EMAIL = "User with this e-mail already exists.";
    public static final String DUPLICATED_USER_ROLE = "User role with this name already exists.";
    public static final String DUPLICATED_PERSON = "User with this person ID already exists.";
}
