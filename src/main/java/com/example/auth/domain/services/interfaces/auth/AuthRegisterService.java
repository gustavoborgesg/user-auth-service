package com.example.auth.domain.services.interfaces.auth;

import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.user.UserDTO;

public interface AuthRegisterService {
    UserDTO register(UserRegisterDTO data);

}
