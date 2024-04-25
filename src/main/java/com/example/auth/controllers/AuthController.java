package com.example.auth.controllers;

import com.example.auth.domain.dtos.auth.UserFullRegisterDTO;
import com.example.auth.domain.dtos.auth.UserLoginDTO;
import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.services.interfaces.auth.AuthFullRegisterService;
import com.example.auth.domain.services.interfaces.auth.AuthLoginService;
import com.example.auth.domain.services.interfaces.auth.AuthRegisterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private AuthLoginService authLoginService;
    private AuthRegisterService authRegisterService;
    private AuthFullRegisterService authFullRegisterService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginUser(@RequestBody UserLoginDTO userData, HttpServletResponse response) {
        return authLoginService.login(userData, response);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@RequestBody UserRegisterDTO userData) {
        return authRegisterService.register(userData);
    }

    @PostMapping("/register/full")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUserFull(@RequestBody UserFullRegisterDTO userData) {
        return authFullRegisterService.registerFull(userData);
    }
}
