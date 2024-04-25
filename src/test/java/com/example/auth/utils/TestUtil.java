package com.example.auth.utils;

import com.example.auth.domain.dtos.auth.UserFullRegisterDTO;
import com.example.auth.domain.dtos.auth.UserLoginDTO;
import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonGetDTO;
import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.dtos.user.UserGetDTO;
import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.dtos.user.UserModificationDTO;
import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.dtos.user.role.UserRoleModificationDTO;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;

public class TestUtil {
    public static final Long DEFAULT_USER_ID = 1L;
    public static final Long DEFAULT_ROLE_ID = 1L;
    public static final Long DEFAULT_PERSON_ID = 1L;
    private static final Long DEFAULT_OLDUSER_ID = 1L;
    private static final String DEFAULT_USERNAME = "defaultUser";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_ROLE_NAME = "ROLE_ADMIN";
    private static final String DEFAULT_PERSON_NAME = "John Doe";
    private static final String DEFAULT_CPF = "12345678901";
    private static final String DEFAULT_EMAIL = "john.doe@example.com";

    public static User createNewUser() {
        UserRole role = createNewUserRole();
        Person person = createNewPerson();
        User user = User.builder()
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .role(role)
                .enabled(true)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .person(person)
                .build();
        user.setId(DEFAULT_USER_ID);
        return user;
    }

    public static UserLoginDTO createNewUserLoginDTO() {
        return UserLoginDTO.builder()
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .build();
    }

    public static UserDTO createNewUserDTO() {
        UserRoleDTO role = createNewUserRoleDTO();
        PersonDTO person = createNewPersonDTO();
        return UserDTO.builder()
                .id(DEFAULT_USER_ID)
                .username(DEFAULT_USERNAME)
                .role(role)
                .enabled(true)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .person(person)
                .build();
    }

    public static UserFullRegisterDTO createNewUserFullRegisterDTO() {
        PersonModificationDTO person = createNewPersonModificationDTO();
        return UserFullRegisterDTO.builder()
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .roleName(DEFAULT_ROLE_NAME)
                .person(person)
                .build();
    }

    public static UserRegisterDTO createNewUserRegisterDTO() {
        return UserRegisterDTO.builder()
                .idOldUser(DEFAULT_OLDUSER_ID)
                .username(DEFAULT_USERNAME)
                .password(DEFAULT_PASSWORD)
                .roleName(DEFAULT_ROLE_NAME)
                .personID(DEFAULT_PERSON_ID)
                .build();
    }

    public static UserModificationDTO createNewUserModificationDTO() {
        PersonModificationDTO person = createNewPersonModificationDTO();
        return UserModificationDTO.builder()
                .username(DEFAULT_USERNAME)
                .roleName(DEFAULT_ROLE_NAME)
                .enabled(true)
                .person(person)
                .build();
    }

    public static UserGetDTO createNewUserGetDTO() {
        PersonGetDTO person = createNewPersonGetDTO();
        return UserGetDTO.builder()
                .id(DEFAULT_USER_ID)
                .username(DEFAULT_USERNAME)
                .enabled(true)
                .roleName(DEFAULT_ROLE_NAME)
                .person(person)
                .build();
    }

    public static UserGetListFiltersDTO createNewUserGetFiltersDTO() {
        return UserGetListFiltersDTO.builder()
                .username(DEFAULT_USERNAME)
                .enabled(true)
                .roleName(DEFAULT_ROLE_NAME)
                .cpf(DEFAULT_CPF)
                .name(DEFAULT_PERSON_NAME)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static UserRole createNewUserRole() {
        UserRole userRole = UserRole.builder()
                .roleName(DEFAULT_ROLE_NAME)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();
        userRole.setId(DEFAULT_ROLE_ID);
        return userRole;
    }

    public static UserRoleDTO createNewUserRoleDTO() {
        return UserRoleDTO.builder()
                .id(DEFAULT_ROLE_ID)
                .roleName(DEFAULT_ROLE_NAME)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();
    }

    public static UserRoleModificationDTO createNewUserRoleModificationDTO() {
        return UserRoleModificationDTO.builder()
                .roleName(DEFAULT_ROLE_NAME)
                .build();
    }

    public static Person createNewPerson() {
        Person person = Person.builder()
                .name(DEFAULT_PERSON_NAME)
                .cpf(DEFAULT_CPF)
                .email(DEFAULT_EMAIL)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();
        person.setId(DEFAULT_PERSON_ID);
        return person;
    }

    public static PersonDTO createNewPersonDTO() {
        return PersonDTO.builder()
                .id(DEFAULT_PERSON_ID)
                .name(DEFAULT_PERSON_NAME)
                .cpf(DEFAULT_CPF)
                .email(DEFAULT_EMAIL)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();
    }

    public static PersonModificationDTO createNewPersonModificationDTO() {
        return PersonModificationDTO.builder()
                .name(DEFAULT_PERSON_NAME)
                .cpf(DEFAULT_CPF)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static PersonGetDTO createNewPersonGetDTO() {
        return PersonGetDTO.builder()
                .id(DEFAULT_PERSON_ID)
                .name(DEFAULT_PERSON_NAME)
                .cpf(DEFAULT_CPF)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static PersonGetFiltersDTO createNewPersonGetFiltersDTO() {
        return PersonGetFiltersDTO.builder()
                .name(DEFAULT_PERSON_NAME)
                .cpf(DEFAULT_CPF)
                .email(DEFAULT_EMAIL)
                .build();
    }
}
