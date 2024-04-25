package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.dtos.user.role.UserRoleModificationDTO;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRoleException userRoleException;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private UserRole userRole;
    private UserRoleDTO userRoleResult;
    private UserRoleModificationDTO userRoleData;

    @BeforeEach
    void setUp() {
        userRole = createNewUserRole();
        userRoleData = createNewUserRoleModificationDTO();
        userRoleResult = createNewUserRoleDTO();
    }

    @Test
    void register_success() {
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

        when(modelMapper.map(eq(userRole), eq(UserRoleDTO.class))).thenReturn(userRoleResult);

        UserRoleDTO result = userRoleService.register(userRoleData);

        assertNotNull(result);
        assertEquals(userRoleResult.getRoleName(), result.getRoleName());
    }

    @Test
    void register_fail_userRoleNameNotPresent() {
        userRoleData.setRoleName("");

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_ROLE, "", HttpStatus.BAD_REQUEST, false))
                .when(userRoleException).exceptionEmptyUserRoleName();

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.register(userRoleData));

        verify(userRoleRepository, never()).save(any(UserRole.class));
    }

    @Test
    void register_fail_userRoleNameNotUnique() {
        when(userRoleRepository.findByRoleName(userRole.getRoleName())).thenReturn(Optional.of(userRole));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_USER_ROLE, "", HttpStatus.CONFLICT, false))
                .when(userRoleException).exceptionDuplicatedUserRoleName();

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.register(userRoleData));

        verify(userRoleRepository, never()).save(any(UserRole.class));
    }

    @Test
    public void register_fail_databaseConnection() {
        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(userRoleException).exceptionDatabaseError();
        when(userRoleRepository.save(any(UserRole.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.register(userRoleData));
    }

    @Test
    void update_success_roleName() {
        String newRoleName = "ROLE_USER";

        userRoleData.setRoleName(newRoleName);

        userRoleResult.setRoleName(newRoleName);
        userRoleResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(userRoleRepository.findById(DEFAULT_ROLE_ID)).thenReturn(Optional.of(userRole));
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

        when(modelMapper.map(eq(userRole), eq(UserRoleDTO.class))).thenReturn(userRoleResult);

        UserRoleDTO result = userRoleService.update(DEFAULT_ROLE_ID, userRoleData);

        assertNotNull(result);
        assertEquals(userRoleResult.getRoleName(), result.getRoleName());
        assertEquals(DEFAULT_ROLE_ID, result.getId());
    }

    @Test
    void update_fail_userRoleNameNotUnique() {
        when(userRoleRepository.findByRoleName(userRoleResult.getRoleName())).thenReturn(Optional.of(userRole));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_USER_ROLE, "", HttpStatus.CONFLICT, false))
                .when(userRoleException).exceptionDuplicatedUserRoleName();

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.update(DEFAULT_ROLE_ID, userRoleData));
    }

    @Test
    void update_fail_nonExistentUserRole() {
        Long userRoleNonExistentID = 2L;

        when(userRoleRepository.findById(userRoleNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_ROLE, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.update(userRoleNonExistentID, userRoleData));
    }

    @Test
    public void update_fail_databaseConnection() {
        String newRoleName = "ROLE_USER";

        userRoleData.setRoleName(newRoleName);

        userRoleResult.setRoleName(newRoleName);
        userRoleResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(userRoleRepository.findById(DEFAULT_ROLE_ID)).thenReturn(Optional.of(userRole));

        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(userRoleException).exceptionDatabaseError();
        when(userRoleRepository.save(any(UserRole.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.update(DEFAULT_ROLE_ID, userRoleData));
    }

    @Test
    void get_success() {
        when(userRoleRepository.findById(DEFAULT_ROLE_ID)).thenReturn(Optional.of(userRole));
        when(modelMapper.map(eq(userRole), eq(UserRoleDTO.class))).thenReturn(userRoleResult);
        UserRoleDTO result = userRoleService.get(DEFAULT_ROLE_ID);

        assertNotNull(result);
        assertEquals(userRoleResult, result);
    }

    @Test
    void get_fail_nonExistentUserRole() {
        Long userRoleNonExistentID = 2L;

        when(userRoleRepository.findById(userRoleNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_ROLE, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> userRoleService.get(userRoleNonExistentID));
    }

    @Test
    void getList_success() {
        List<UserRole> listedUserRoles = Collections.singletonList(userRole);

        List<UserRoleDTO> userRoleResultList = listedUserRoles.stream()
                .map(userRole -> createNewUserRoleDTO())
                .toList();

        when(userRoleRepository.findAll()).thenReturn(listedUserRoles);

        when(modelMapper.map(any(UserRole.class), eq(UserRoleDTO.class)))
                .thenAnswer(invocation -> createNewUserRoleDTO());

        List<UserRoleDTO> resultList = userRoleService.getList();

        assertEquals(userRoleResultList.size(), resultList.size());
        assertTrue(resultList.containsAll(userRoleResultList));
    }

    @Test
    void getList_success_empty() {
        when(userRoleRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserRoleDTO> result = userRoleService.getList();

        assertTrue(result.isEmpty());
    }
}
