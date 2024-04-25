package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.exceptions.PersonException;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.repositories.PersonRepository;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.repositories.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRegisterServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private AuthException authException;
    @Mock
    private UserException userException;
    @Mock
    private PersonException personException;
    @Mock
    private UserRoleException userRoleException;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthRegisterServiceImpl authRegisterService;

    private User user;
    private UserDTO userResult;
    private UserRegisterDTO userRegisterData;
    private UserRole userRole;
    private Person person;

    @BeforeEach
    void setUp() {
        user = createNewUser();
        userResult = createNewUserDTO();
        userRegisterData = createNewUserRegisterDTO();

        userRole = createNewUserRole();

        person = createNewPerson();
    }

    @Test
    void register_success() {
        when(userRepository.findByUsername(userRegisterData.getUsername())).thenReturn(Optional.of(user));
        when(userRoleRepository.findByRoleName(userRegisterData.getRoleName())).thenReturn(Optional.of(userRole));
        when(personRepository.findById(userRegisterData.getPersonID())).thenReturn(Optional.of(person));
        when(userRepository.findByPersonId(person.getId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegisterData.getPassword())).thenReturn("123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userResult);

        UserDTO result = authRegisterService.register(userRegisterData);

        assertNotNull(result);
        assertEquals(userResult, result);
    }

    @Test
    void register_fail_usernameNotPresent() {
        userRegisterData.setUsername("");

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_USERNAME, "", HttpStatus.BAD_REQUEST, false))
                .when(authException).exceptionEmptyUsername();

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_usernameNotUnique() {
        when(userRepository.findByUsername(userRegisterData.getUsername())).thenReturn(Optional.of(user));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_CPF, "", HttpStatus.CONFLICT, false))
                .when(authException).exceptionDuplicatedUsername();

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_passwordNotPresent() {
        userRegisterData.setPassword("");
        when(userRepository.findByUsername(userRegisterData.getUsername())).thenReturn(Optional.of(user));

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_PASSWORD, "", HttpStatus.BAD_REQUEST, false))
                .when(authException).exceptionEmptyPassword();

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_roleNotPresent() {
        userRegisterData.setRoleName("");

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_ROLE, "", HttpStatus.BAD_REQUEST, false))
                .when(userRoleException).exceptionEmptyUserRoleName();

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_roleDoesNotExist() {
        userRegisterData.setRoleName("ROLE_TEST");

        when(userRoleRepository.findByRoleName(userRegisterData.getRoleName()))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_ROLE, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_personDoesNotExist() {
        Long personNonExistentID = 2L;
        userRegisterData.setPersonID(personNonExistentID);

        when(userRoleRepository.findByRoleName(userRegisterData.getRoleName())).thenReturn(Optional.of(userRole));

        when(personRepository.findById(personNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_PERSON, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_fail_personNotUnique() {
        when(personRepository.findById(userRegisterData.getPersonID())).thenReturn(Optional.of(person));

        when(userRepository.findByPersonId(person.getId())).thenReturn(Optional.of(user));

        when(userRoleRepository.findByRoleName(userRegisterData.getRoleName())).thenReturn(Optional.of(userRole));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_PERSON, "", HttpStatus.CONFLICT, false))
                .when(userException).exceptionDuplicatedPerson();

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void register_fail_databaseConnection() {
        when(userRepository.findByUsername(userRegisterData.getUsername())).thenReturn(Optional.of(user));
        when(userRoleRepository.findByRoleName(userRegisterData.getRoleName())).thenReturn(Optional.of(userRole));
        when(personRepository.findById(userRegisterData.getPersonID())).thenReturn(Optional.of(person));
        when(passwordEncoder.encode(userRegisterData.getPassword())).thenReturn("123");

        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(authException).exceptionDatabaseError();
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> authRegisterService.register(userRegisterData));
    }
}