package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserFullRegisterDTO;
import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.services.interfaces.PersonService;
import com.example.auth.domain.services.interfaces.auth.AuthFullRegisterService;
import com.example.auth.domain.services.interfaces.auth.AuthRegisterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthFullRegisterServiceImpl implements AuthFullRegisterService {

    private final AuthRegisterService authRegisterService;
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDTO registerFull(UserFullRegisterDTO fullUserData) {
        PersonModificationDTO personData = modelMapper.map(fullUserData.getPerson(), PersonModificationDTO.class);
        PersonDTO personDTO = personService.register(personData);

        UserRegisterDTO userData = UserRegisterDTO.builder()
                .idOldUser(fullUserData.getIdOldUser())
                .username(fullUserData.getUsername())
                .password(fullUserData.getPassword())
                .roleName(fullUserData.getRoleName())
                .personID(personDTO.getId())
                .build();

        return authRegisterService.register(userData);
    }
}
