package com.example.auth.domain.services.interfaces;

import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.dtos.user.role.UserRoleModificationDTO;

import java.util.List;

public interface UserRoleService {
    UserRoleDTO register(UserRoleModificationDTO userRoleData);

    UserRoleDTO update(Long id, UserRoleModificationDTO userRoleData);

    UserRoleDTO get(Long id);

    List<UserRoleDTO> getList();
}
