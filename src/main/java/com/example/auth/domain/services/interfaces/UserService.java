package com.example.auth.domain.services.interfaces;

import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.dtos.user.UserGetDTO;
import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.dtos.user.UserModificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO update(Long id, UserModificationDTO userData);

    UserDTO get(Long id);

    Page<UserGetDTO> getList(UserGetListFiltersDTO filters, Pageable pageable);
}
