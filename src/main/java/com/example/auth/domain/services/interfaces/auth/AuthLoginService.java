package com.example.auth.domain.services.interfaces.auth;

import com.example.auth.domain.dtos.auth.UserLoginDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthLoginService {
    String login(UserLoginDTO data, HttpServletResponse response);
}
