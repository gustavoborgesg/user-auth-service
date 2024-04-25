package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.dtos.user.role.UserRoleModificationDTO;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.services.interfaces.UserRoleService;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.repositories.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleException userRoleException;
    private final ModelMapper modelMapper;

    @Override
    public UserRoleDTO register(UserRoleModificationDTO userRoleData) {
        if (!Utils.isStringPresent(userRoleData.getRoleName())) {
            userRoleException.exceptionEmptyUserRoleName();
        }

        userRoleRepository.findByRoleName(userRoleData.getRoleName())
                .ifPresent((duplicatedUser) -> userRoleException.exceptionDuplicatedUserRoleName());

        UserRole userRole = UserRole.builder()
                .roleName(userRoleData.getRoleName())
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();

        try {
            userRole = userRoleRepository.save(userRole);
        } catch (Exception exception) {
            userRoleException.exceptionDatabaseError();
        }
        return modelMapper.map(userRole, UserRoleDTO.class);
    }

    @Override
    public UserRoleDTO update(Long id, UserRoleModificationDTO userRoleData) {
        userRoleRepository.findByRoleName(userRoleData.getRoleName())
                .ifPresent((duplicatedUser) -> userRoleException.exceptionDuplicatedUserRoleName());

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(userRoleException::exceptionNonExistentUserRole);

        userRole.setRoleName(userRoleData.getRoleName());
        userRole.setDateAlteration(Utils.getDateTimeNowFormatted());

        try {
            userRole = userRoleRepository.save(userRole);
        } catch (Exception exception) {
            userRoleException.exceptionDatabaseError();
        }
        return modelMapper.map(userRole, UserRoleDTO.class);
    }

    @Override
    public UserRoleDTO get(Long id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(userRoleException::exceptionNonExistentUserRole);

        return modelMapper.map(userRole, UserRoleDTO.class);
    }

    @Override
    public List<UserRoleDTO> getList() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoles.stream().map(userRole -> modelMapper.map(userRole, UserRoleDTO.class)).collect(Collectors.toList());
    }

}
