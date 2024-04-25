package com.example.auth.infra.security.auth;

import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private AuthException authException;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    private Authentication authentication;
    private MyUserDetails myUserDetails;

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

        myUserDetails = new MyUserDetails(user);
        authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }

    @Test
    void authenticate_success() {
        when(myUserDetailsService.loadUserByUsername(authentication.getName())).thenReturn(myUserDetails);
        when(passwordEncoder.matches(authentication.getCredentials().toString(), myUserDetails.getPassword())).thenReturn(true);

        Authentication result = customAuthenticationProvider.authenticate(authentication);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals(user.getUsername(), result.getName());
    }

    @Test
    void authenticate_fail_emptyUsername() {
        String emptyUsername = "";
        authentication = new UsernamePasswordAuthenticationToken(emptyUsername, user.getPassword());

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_USERNAME, "", HttpStatus.BAD_REQUEST, false))
                .when(authException).exceptionEmptyUsername();

        assertThrows(CustomExceptionRequest.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    void authenticate_fail_invalidPassword() {
        String wrongPassword = "wrongPassword";
        authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), wrongPassword);

        when(myUserDetailsService.loadUserByUsername(authentication.getName())).thenReturn(myUserDetails);
        when(passwordEncoder.matches(authentication.getCredentials().toString(), myUserDetails.getPassword())).thenReturn(false);

        doThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_AUTHENTICATION, "", HttpStatus.UNAUTHORIZED, false))
                .when(authException).exceptionInvalidPassword();

        assertThrows(CustomExceptionRequest.class, () -> customAuthenticationProvider.authenticate(authentication));
    }
}