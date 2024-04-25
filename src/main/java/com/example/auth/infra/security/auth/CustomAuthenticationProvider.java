package com.example.auth.infra.security.auth;

import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.auth.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService myUserDetailsService;
    private final AuthException authException;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (!Utils.isStringPresent(username)) {
            authException.exceptionEmptyUsername();
        }

        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            authException.exceptionInvalidPassword();
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}