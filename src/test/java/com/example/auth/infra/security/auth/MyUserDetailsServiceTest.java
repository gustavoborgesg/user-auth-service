package com.example.auth.infra.security.auth;

import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthException authException;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private Long userID;
    private User user;
    private Person person;
    private UserRole userRole;

    @BeforeEach
    void setUp() {
        userID = 1L;

        userRole = createNewUserRole();
        userRole.setId(userID);

        person = createNewPerson();
        person.setId(userID);

        user = createNewUser();
        user.setId(userID);
    }

    @Test
    void loadUserByUsername_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());

        assertNotNull(userDetails);
    }


    @Test
    void loadUserByUsername_fail_nonExistentUser() {
        when(userRepository.findByUsername(anyString()))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_AUTHENTICATION, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> myUserDetailsService.loadUserByUsername("userNonExistentID"));
    }
}