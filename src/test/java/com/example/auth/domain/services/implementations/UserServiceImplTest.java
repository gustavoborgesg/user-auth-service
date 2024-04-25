package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.dtos.user.UserGetDTO;
import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.dtos.user.UserModificationDTO;
import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.domain.services.interfaces.PersonService;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.repositories.UserRoleRepository;
import com.example.auth.infra.repositories.custom.interfaces.UserCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PersonService personService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCustomRepository userCustomRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private AuthException authException;
    @Mock
    private UserException userException;
    @Mock
    private UserRoleException userRoleException;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private String newUserRoleName;
    private User user;
    private UserDTO userResult;
    private UserModificationDTO userData;
    private UserGetListFiltersDTO userFilters;
    private Person person;
    private PersonDTO personResult;
    private PersonModificationDTO personData;
    private UserRole userRole;
    private UserRoleDTO userRoleResult;

    @BeforeEach
    void setUp() {
        newUserRoleName = "ROLE_USER";

        user = createNewUser();
        userResult = createNewUserDTO();
        userData = createNewUserModificationDTO();

        userRole = createNewUserRole();
        userRoleResult = createNewUserRoleDTO();

        person = createNewPerson();
        personResult = createNewPersonDTO();
        personData = createNewPersonModificationDTO();

        userFilters = createNewUserGetFiltersDTO();
    }

    @Test
    void update_success_allData() {
        String newUsername = "john";

        String newName = "Jane Doe";
        String newCPF = "98765432109";
        String newEmail = "jane.doe@example.com";

        personData.setName(newName);
        personData.setCpf(newCPF);
        personData.setEmail(newEmail);

        userData.setUsername(newUsername);
        userData.setRoleName(newUserRoleName);
        userData.setPerson(personData);

        userRoleResult.setRoleName(newUserRoleName);
        userRoleResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        personResult.setName(newName);
        personResult.setCpf(newCPF);
        personResult.setEmail(newEmail);
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        userResult.setUsername(newUsername);
        userResult.setRole(userRoleResult);
        userResult.setPerson(personResult);
        userResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));

        when(userRoleRepository.findByRoleName(userData.getRoleName())).thenReturn(Optional.ofNullable(userRole));

        when(personService.update(DEFAULT_USER_ID, personData)).thenReturn(personResult);

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(modelMapper.map(eq(user), eq(UserDTO.class))).thenReturn(userResult);

        UserDTO result = userService.update(DEFAULT_USER_ID, userData);

        assertNotNull(result);
        assertEquals(userResult, result);
        assertEquals(DEFAULT_USER_ID, result.getId());
    }

    @Test
    void update_success_userRoleAndEmailOnly() {
        String newEmail = "jane.doe@example.com";

        personData.setEmail(newEmail);

        userData.setRoleName(newUserRoleName);
        userData.setPerson(personData);

        userRoleResult.setRoleName(newUserRoleName);
        userRoleResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        personResult.setEmail(newEmail);
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        userResult.setRole(userRoleResult);
        userResult.setPerson(personResult);
        userResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));

        when(userRoleRepository.findByRoleName(userData.getRoleName())).thenReturn(Optional.ofNullable(userRole));

        when(personService.update(DEFAULT_USER_ID, personData)).thenReturn(personResult);

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(modelMapper.map(eq(user), eq(UserDTO.class))).thenReturn(userResult);

        UserDTO result = userService.update(DEFAULT_USER_ID, userData);

        assertNotNull(result);
        assertEquals(userResult, result);
        assertEquals(DEFAULT_USER_ID, result.getId());
    }

    @Test
    void update_fail_nonExistentUser() {
        Long userNonExistentID = 2L;

        when(userRepository.findById(userNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_USER, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> userService.update(userNonExistentID, userData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_fail_usernameNotUnique() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));

        when(userRepository.findByUsername(userData.getUsername())).thenReturn(Optional.of(user));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_USERNAME, "", HttpStatus.CONFLICT, false))
                .when(authException).exceptionDuplicatedUsername();

        assertThrows(CustomExceptionRequest.class, () -> userService.update(DEFAULT_USER_ID, userData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_fail_nonExistentUserRole() {
        userData.setRoleName("ROLE_TEST");

        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));

        when(userRoleRepository.findByRoleName(userData.getRoleName()))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_ROLE, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> userService.update(DEFAULT_USER_ID, userData));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void update_fail_databaseConnection() {
        String newUsername = "john";

        String newName = "Jane Doe";
        String newCPF = "98765432109";
        String newEmail = "jane.doe@example.com";

        personData.setName(newName);
        personData.setCpf(newCPF);
        personData.setEmail(newEmail);

        userData.setUsername(newUsername);
        userData.setRoleName(newUserRoleName);
        userData.setPerson(personData);

        userRoleResult.setRoleName(newUserRoleName);
        userRoleResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        personResult.setName(newName);
        personResult.setCpf(newCPF);
        personResult.setEmail(newEmail);
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        userResult.setUsername(newUsername);
        userResult.setRole(userRoleResult);
        userResult.setPerson(personResult);
        userResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));
        when(userRoleRepository.findByRoleName(userData.getRoleName())).thenReturn(Optional.ofNullable(userRole));
        when(personService.update(DEFAULT_USER_ID, personData)).thenReturn(personResult);

        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(userException).exceptionDatabaseError();
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> userService.update(DEFAULT_USER_ID, userData));
    }

    @Test
    void get_success() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));
        when(modelMapper.map(eq(user), eq(UserDTO.class))).thenReturn(userResult);
        UserDTO result = userService.get(DEFAULT_USER_ID);

        assertNotNull(result);
        assertEquals(userResult, result);
    }

    @Test
    void get_fail_invalidID() {
        Long userNonExistentID = 2L;

        when(userRepository.findById(userNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_USER, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> userService.get(userNonExistentID));
    }

    @Test
    void getList_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

        when(userCustomRepository.findWithFilters(any(UserGetListFiltersDTO.class), any(Pageable.class))).thenReturn(page);

        Page<UserGetDTO> resultPage = userService.getList(userFilters, pageable);

        assertNotNull(resultPage);
        assertFalse(resultPage.getContent().isEmpty());
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void getList_success_empty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = Page.empty(pageable);

        when(userCustomRepository.findWithFilters(any(UserGetListFiltersDTO.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<UserGetDTO> resultPage = userService.getList(userFilters, pageable);

        assertNotNull(resultPage);
        assertTrue(resultPage.getContent().isEmpty());
        assertEquals(0, resultPage.getTotalElements());
    }
}