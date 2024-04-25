package com.example.auth.domain.services.interfaces.auth;

import com.example.auth.domain.dtos.auth.UserFullRegisterDTO;
import com.example.auth.domain.dtos.user.UserDTO;

public interface AuthFullRegisterService {
    UserDTO registerFull(UserFullRegisterDTO fullUserData);
}
