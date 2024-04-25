package com.example.auth.domain.services.implementations.auth;

import com.example.auth.domain.dtos.auth.UserFullRegisterDTO;
import com.example.auth.domain.dtos.auth.UserRegisterDTO;
import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.services.interfaces.PersonService;
import com.example.auth.domain.services.interfaces.auth.AuthRegisterService;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.repositories.PersonRepository;
import com.example.auth.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFullRegisterServiceImplTest {

    @Mock
    private AuthRegisterService authRegisterService;
    @Mock
    private PersonService personService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private AuthFullRegisterServiceImpl fullRegisterService;

    private UserDTO userResult;
    private UserFullRegisterDTO userFullRegisterData;
    private PersonDTO personResult;
    private PersonModificationDTO personData;

    @BeforeEach
    void setUp() {
        userResult = createNewUserDTO();
        userFullRegisterData = createNewUserFullRegisterDTO();

        personResult = createNewPersonDTO();
        personData = createNewPersonModificationDTO();
    }

    @Test
    void registerFull_success() {
        when(modelMapper.map(eq(userFullRegisterData.getPerson()), eq(PersonModificationDTO.class))).thenReturn(personData);
        when(personService.register(eq(personData))).thenReturn(personResult);

        when(authRegisterService.register(any(UserRegisterDTO.class))).thenReturn(userResult);

        UserDTO result = fullRegisterService.registerFull(userFullRegisterData);

        assertNotNull(result);
        assertEquals(userResult, result);
    }

    @Test
    void registerFull_fail_personRegister() {
        when(modelMapper.map(eq(userFullRegisterData.getPerson()), eq(PersonModificationDTO.class))).thenReturn(personData);
        when(personService.register(eq(personData))).thenThrow(new RuntimeException("Person registration failed"));

        assertThrows(RuntimeException.class, () -> fullRegisterService.registerFull(userFullRegisterData));

        verify(personRepository, never()).save(any(Person.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerFull_fail_userRegister() {
        when(modelMapper.map(eq(userFullRegisterData.getPerson()), eq(PersonModificationDTO.class))).thenReturn(personData);
        when(personService.register(eq(personData))).thenReturn(personResult);
        when(authRegisterService.register(any(UserRegisterDTO.class))).thenThrow(new RuntimeException("User registration failed"));

        assertThrows(RuntimeException.class, () -> fullRegisterService.registerFull(userFullRegisterData));

        verify(personRepository, never()).save(any(Person.class));
        verify(userRepository, never()).save(any(User.class));
    }
}