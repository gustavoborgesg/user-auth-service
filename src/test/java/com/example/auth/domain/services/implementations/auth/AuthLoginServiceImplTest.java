package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserLoginDTO;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.security.auth.CookieService;
import com.example.auth.infra.security.auth.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static com.example.auth.utils.TestUtil.createNewUser;
import static com.example.auth.utils.TestUtil.createNewUserLoginDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthLoginServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private Authentication authentication;
    @Mock
    private TokenService tokenService;
    @Mock
    private CookieService cookieService;
    @Mock
    private UserException userException;

    @InjectMocks
    private AuthLoginServiceImpl userLoginService;

    private String token;
    private User user;
    private UserLoginDTO userLogin;
    private MyUserDetails myUserDetails;

    @BeforeEach
    void setUp() {
        token = "generatedToken";

        user = createNewUser();
        userLogin = createNewUserLoginDTO();

        myUserDetails = new MyUserDetails(user);
    }

    @Test
    void login_success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(myUserDetails);
        when(tokenService.generateToken(any(User.class))).thenReturn(token);

        String responseMessage = userLoginService.login(userLogin, httpServletResponse);

        assertEquals("Login successfully!", responseMessage);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(any(User.class));
        verify(cookieService).createCookieWithToken(eq(token), eq(httpServletResponse));
    }

    @Test
    void login_fail_notAuthenticated() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Authentication failed") {
                });

        org.springframework.security.core.AuthenticationException thrown = assertThrows(
                org.springframework.security.core.AuthenticationException.class,
                () -> userLoginService.login(userLogin, httpServletResponse),
                "Expected login to throw an authentication exception, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Authentication failed"));
    }

    @Test
    void login_fail_userNotEnabled() {
        user.setEnabled(false);
        myUserDetails = new MyUserDetails(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(myUserDetails);

        doThrow(new CustomExceptionRequest(ExceptionMessages.DISABLED_USER, "", HttpStatus.FORBIDDEN, false))
                .when(userException).exceptionDisabledUser();

        CustomExceptionRequest thrown = assertThrows(CustomExceptionRequest.class, () -> userLoginService.login(userLogin, httpServletResponse));

        assertTrue(thrown.getMessage().contains(ExceptionMessages.DISABLED_USER));
    }
}