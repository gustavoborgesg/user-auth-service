package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.dtos.user.UserGetDTO;
import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.dtos.user.UserModificationDTO;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.domain.services.interfaces.PersonService;
import com.example.auth.domain.services.interfaces.UserService;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.repositories.UserRoleRepository;
import com.example.auth.infra.repositories.custom.interfaces.UserCustomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonService personService;
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthException authException;
    private final UserException userException;
    private final UserRoleException userRoleException;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDTO update(Long id, UserModificationDTO userData) {

        User user = userRepository.findById(id)
                .orElseThrow(userException::exceptionNonExistentUser);

        if (Utils.isStringPresent(userData.getUsername())) {
            userRepository.findByUsername(userData.getUsername())
                    .ifPresent((duplicatedUser) -> authException.exceptionDuplicatedUsername());

            user.setUsername(userData.getUsername());
        }

        if (userData.getEnabled() != null) {
            if (userData.getEnabled().compareTo(user.getEnabled()) != 0) {
                user.setEnabled(userData.getEnabled());
            }
        }

        if (Utils.isStringPresent(userData.getRoleName())) {
            UserRole userRole = userRoleRepository.findByRoleName(userData.getRoleName())
                    .orElseThrow(userRoleException::exceptionNonExistentUserRole);
            user.setRole(userRole);
        }

        if (userData.getPerson() != null) {
            Person person = user.getPerson();
            PersonModificationDTO personData = userData.getPerson();
            personService.update(person.getId(), personData);
        }

        user.setDateAlteration(Utils.getDateTimeNowFormatted());

        try {
            user = userRepository.save(user);
        } catch (Exception exception) {
            userException.exceptionDatabaseError();
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(userException::exceptionNonExistentUser);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public Page<UserGetDTO> getList(UserGetListFiltersDTO userFilters, Pageable pageable) {
        Page<User> userPage = userCustomRepository.findWithFilters(userFilters, pageable);

        return new PageImpl<>(userPage.stream()
                .map(user -> modelMapper.map(user, UserGetDTO.class))
                .collect(Collectors.toList()), pageable, userPage.getTotalElements());
    }
}
