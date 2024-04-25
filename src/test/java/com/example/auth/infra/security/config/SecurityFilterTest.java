package com.example.auth.infra.security.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.security.auth.CookieService;
import com.example.auth.infra.security.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private CookieService cookieService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthException authException;
    @Mock
    private DecodedJWT decodedJWT;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    private String tokenValue;
    private Long userID;
    private User user;
    private Person person;
    private UserRole userRole;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        tokenValue = "validToken";

        userID = 1L;
        userRole = createNewUserRole();
        userRole.setId(userID);

        person = createNewPerson();
        person.setId(userID);

        user = createNewUser();
        user.setId(userID);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
        when(request.getRequestURI()).thenReturn("/anyPath");
    }

    @Test
    void doFilterInternal_success_login() throws Exception {
        when(request.getRequestURI()).thenReturn("/auth/login");

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void doFilterInternal_success_anyPath() throws Exception {
        when(cookieService.recoverTokenFromCookie(request)).thenReturn(tokenValue);
        when(tokenService.validateToken(tokenValue)).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn(user.getUsername());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(user.getUsername(), ((MyUserDetails) authentication.getPrincipal()).getUsername());

        boolean hasRoleAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        assertTrue(hasRoleAdmin);
    }

    @Test
    void doFilterInternal_fail_nullToken() {
        tokenValue = "";
        when(cookieService.recoverTokenFromCookie(request)).thenReturn(tokenValue);

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_TOKEN, "", HttpStatus.BAD_REQUEST, false))
                .when(authException).exceptionEmptyToken();

        assertThrows(CustomExceptionRequest.class, () -> securityFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    void doFilterInternal_fail_tokenInvalid() {
        tokenValue = "invalidToken";

        when(cookieService.recoverTokenFromCookie(request)).thenReturn(tokenValue);
        when(tokenService.validateToken(tokenValue))
                .thenThrow(new JWTVerificationException("Error while validating token", new Exception("Cause of failure")));

        JWTVerificationException thrownException =
                assertThrows(JWTVerificationException.class, () -> securityFilter.doFilterInternal(request, response, filterChain));

        assertEquals("Error while validating token", thrownException.getMessage());
        assertNotNull(thrownException.getCause());
        assertEquals("Cause of failure", thrownException.getCause().getMessage());
    }

    @Test
    void doFilterInternal_fail_userNotPresent() {
        when(cookieService.recoverTokenFromCookie(request)).thenReturn(tokenValue);
        when(tokenService.validateToken(tokenValue)).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn(user.getUsername());

        when(userRepository.findByUsername(user.getUsername()))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_AUTHENTICATION, "", HttpStatus.NOT_FOUND, false));
        assertThrows(CustomExceptionRequest.class, () -> securityFilter.doFilterInternal(request, response, filterChain));
    }
}