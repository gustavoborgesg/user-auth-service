package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserLoginDTO;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.domain.services.interfaces.auth.AuthLoginService;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.security.auth.CookieService;
import com.example.auth.infra.security.auth.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AuthLoginServiceImpl implements AuthLoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final UserException userException;

    @Override
    public String login(UserLoginDTO userData, HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userData.getUsername(), userData.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        User user = myUserDetails.getUser();
        if (!user.getEnabled()) {
            userException.exceptionDisabledUser();
        }

        String token = tokenService.generateToken(user);
        cookieService.createCookieWithToken(token, httpServletResponse);

        return "Login successfully!";
    }
}
