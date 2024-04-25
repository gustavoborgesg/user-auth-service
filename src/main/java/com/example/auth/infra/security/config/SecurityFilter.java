package com.example.auth.infra.security.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.security.auth.CookieService;
import com.example.auth.infra.security.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final AuthException authException;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/auth/login")) {
            SecurityContextHolder.clearContext();
            cookieService.clearCookie(response);
        } else {
            String token = cookieService.recoverTokenFromCookie(request);
            if (token != null) {
                if (!Utils.isStringPresent(token)) {
                    authException.exceptionEmptyToken();
                }

                DecodedJWT decodedJWT = tokenService.validateToken(token);
                String username = decodedJWT.getSubject();

                User user = userRepository.findByUsername(username)
                        .orElseThrow((authException::exceptionInvalidUsername));

                MyUserDetails myUserDetails = new MyUserDetails(user);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
