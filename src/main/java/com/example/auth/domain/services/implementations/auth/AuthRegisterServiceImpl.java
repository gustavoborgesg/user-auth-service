package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.exceptions.PersonException;
import com.example.auth.domain.exceptions.UserException;
import com.example.auth.domain.exceptions.UserRoleException;
import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.domain.services.interfaces.auth.AuthRegisterService;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.repositories.PersonRepository;
import com.example.auth.infra.repositories.UserRepository;
import com.example.auth.infra.repositories.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthRegisterServiceImpl implements AuthRegisterService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PersonRepository personRepository;

    private final AuthException authException;
    private final UserException userException;
    private final PersonException personException;
    private final UserRoleException userRoleException;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO register(UserRegisterDTO userData) {
        if (!Utils.isStringPresent(userData.getUsername())) {
            authException.exceptionEmptyUsername();
        }

        userRepository.findByUsername(userData.getUsername())
                .ifPresent((duplicatedUser) -> authException.exceptionDuplicatedUsername());

        if (!Utils.isStringPresent(userData.getPassword())) {
            authException.exceptionEmptyPassword();
        }

        if (!Utils.isStringPresent(userData.getRoleName())) {
            userRoleException.exceptionEmptyUserRoleName();
        }

        UserRole userRole = userRoleRepository.findByRoleName(userData.getRoleName())
                .orElseThrow(userRoleException::exceptionNonExistentUserRole);

        Person person = personRepository.findById(userData.getPersonID())
                .orElseThrow(personException::exceptionNonExistentPerson);

        userRepository.findByPersonId(person.getId())
                .ifPresent((user) -> userException.exceptionDuplicatedPerson());

        String encryptedPassword = encryptPassword(userData.getPassword());

        User user = User.builder()
                .username(userData.getUsername())
                .password(encryptedPassword)
                .role(userRole)
                .enabled(true)
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .person(person)
                .build();

        try {
            user = userRepository.save(user);
        } catch (Exception exception) {
            authException.exceptionDatabaseError();
        }

        return modelMapper.map(user, UserDTO.class);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
