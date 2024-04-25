package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonGetDTO;
import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.exceptions.PersonException;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import com.example.auth.infra.exceptions.ExceptionMessages;
import com.example.auth.infra.repositories.PersonRepository;
import com.example.auth.infra.repositories.custom.interfaces.PersonCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.auth.utils.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonCustomRepository personCustomRepository;
    @Mock
    private PersonException personException;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private PersonDTO personResult;
    private PersonModificationDTO personData;
    private PersonGetFiltersDTO personFilters;
    private PersonGetDTO personGetDTO;

    @BeforeEach
    void setUp() {
        person = createNewPerson();
        personResult = createNewPersonDTO();
        personData = createNewPersonModificationDTO();
        personFilters = createNewPersonGetFiltersDTO();
        personGetDTO = createNewPersonGetDTO();
    }

    @Test
    void register_success() {
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(modelMapper.map(eq(person), eq(PersonDTO.class))).thenReturn(personResult);

        PersonDTO result = personService.register(personData);

        assertEquals(personResult, result);
    }

    @Test
    void register_fail_cpfNotPresent() {
        personData.setCpf("");

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_CPF, "", HttpStatus.BAD_REQUEST, false))
                .when(personException).exceptionEmptyCpf();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void register_fail_cpfNotValid() {
        personData.setCpf("123456789019");

        doThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_CPF, "", HttpStatus.BAD_REQUEST, false))
                .when(personException).exceptionInvalidCpf();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void register_fail_cpfNotUnique() {
        when(personRepository.findByCpf(personData.getCpf())).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_CPF, "", HttpStatus.CONFLICT, false))
                .when(personException).exceptionDuplicatedCpf();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void register_fail_emailNotPresent() {
        personData.setEmail("");

        doThrow(new CustomExceptionRequest(ExceptionMessages.EMPTY_EMAIL, "", HttpStatus.BAD_REQUEST, false))
                .when(personException).exceptionEmptyEmail();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void register_fail_emailNotValid() {
        personData.setEmail("john.doe/example.com");

        doThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_EMAIL, "", HttpStatus.UNAUTHORIZED, false))
                .when(personException).exceptionInvalidEmail();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void register_fail_emailNotUnique() {
        when(personRepository.findByEmail(personData.getEmail())).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_EMAIL, "", HttpStatus.CONFLICT, false))
                .when(personException).exceptionDuplicatedEmail();

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void register_fail_databaseConnection() {
        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(personException).exceptionDatabaseError();
        when(personRepository.save(any(Person.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> personService.register(personData));
    }

    @Test
    void update_success_allData() {
        String newName = "Jane Doe";
        String newCPF = "98765432109";
        String newEmail = "jane.doe@example.com";

        personData.setName(newName);
        personData.setCpf(newCPF);
        personData.setEmail(newEmail);

        personResult.setName(newName);
        personResult.setCpf(newCPF);
        personResult.setEmail(newEmail);
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        when(modelMapper.map(eq(person), eq(PersonDTO.class))).thenReturn(personResult);

        PersonDTO result = personService.update(DEFAULT_PERSON_ID, personData);

        assertEquals(personResult, result);
    }

    @Test
    void update_success_emailOnly() {
        personData.setEmail("newEmail@example.com");

        personResult.setEmail("newEmail@example.com");
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        when(modelMapper.map(eq(person), eq(PersonDTO.class))).thenReturn(personResult);

        PersonDTO result = personService.update(DEFAULT_PERSON_ID, personData);

        assertEquals(personResult.getEmail(), result.getEmail());
    }

    @Test
    void update_fail_nonExistentPerson() {
        Long personNonExistentID = 2L;

        when(personRepository.findById(personNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_PERSON, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> personService.update(personNonExistentID, personData));
    }

    @Test
    void update_fail_CpfNotValid() {
        personData.setCpf("123456789019");
        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_CPF, "", HttpStatus.BAD_REQUEST, false))
                .when(personException).exceptionInvalidCpf();

        assertThrows(CustomExceptionRequest.class, () -> personService.update(DEFAULT_PERSON_ID, personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void update_fail_CpfNotUnique() {
        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));

        when(personRepository.findByCpf(personData.getCpf())).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_CPF, "", HttpStatus.CONFLICT, false))
                .when(personException).exceptionDuplicatedCpf();

        assertThrows(CustomExceptionRequest.class, () -> personService.update(DEFAULT_PERSON_ID, personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void update_fail_EmailNotValid() {
        personData.setEmail("john.doe/example.com");
        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.INVALID_EMAIL, "", HttpStatus.BAD_REQUEST, false))
                .when(personException).exceptionInvalidEmail();

        assertThrows(CustomExceptionRequest.class, () -> personService.update(DEFAULT_PERSON_ID, personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void update_fail_EmailNotUnique() {
        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));

        when(personRepository.findByEmail(personData.getEmail())).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.DUPLICATED_EMAIL, "", HttpStatus.CONFLICT, false))
                .when(personException).exceptionDuplicatedEmail();

        assertThrows(CustomExceptionRequest.class, () -> personService.update(DEFAULT_PERSON_ID, personData));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void update_fail_databaseConnection() {
        String newName = "Jane Doe";
        String newCPF = "98765432109";
        String newEmail = "jane.doe@example.com";

        personData.setName(newName);
        personData.setCpf(newCPF);
        personData.setEmail(newEmail);

        personResult.setName(newName);
        personResult.setCpf(newCPF);
        personResult.setEmail(newEmail);
        personResult.setDateAlteration(Utils.getDateTimeNowFormatted());

        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));

        doThrow(new CustomExceptionRequest(ExceptionMessages.ERROR_DATABASE_CONNECTION, "", HttpStatus.INTERNAL_SERVER_ERROR, false))
                .when(personException).exceptionDatabaseError();
        when(personRepository.save(any(Person.class))).thenThrow(new DataAccessException("Database connection error") {
        });

        assertThrows(CustomExceptionRequest.class, () -> personService.update(DEFAULT_PERSON_ID, personData));
    }

    @Test
    void get_success() {
        when(personRepository.findById(DEFAULT_PERSON_ID)).thenReturn(Optional.of(person));
        when(modelMapper.map(eq(person), eq(PersonDTO.class))).thenReturn(personResult);
        PersonDTO result = personService.get(DEFAULT_PERSON_ID);

        assertNotNull(result);
        assertEquals(personResult, result);
    }

    @Test
    void get_fail_invalidID() {
        Long personNonExistentID = 2L;

        when(personRepository.findById(personNonExistentID))
                .thenThrow(new CustomExceptionRequest(ExceptionMessages.NONEXISTENT_PERSON, "", HttpStatus.NOT_FOUND, false));

        assertThrows(CustomExceptionRequest.class, () -> personService.get(personNonExistentID));
    }

    @Test
    void getList_success() {
        Page<Person> personsPage = new PageImpl<>(List.of(person));
        Page<PersonGetDTO> personGetDTOPage = new PageImpl<>(List.of(personGetDTO));

        when(personCustomRepository.findWithFilters(any(PersonGetFiltersDTO.class), any(Pageable.class))).thenReturn(personsPage);
        when(modelMapper.map(any(Person.class), eq(PersonGetDTO.class))).thenReturn(personGetDTO);

        Page<PersonGetDTO> resultPage = personService.getList(personFilters, Pageable.unpaged());

        assertEquals(personGetDTOPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(personGetDTO.getName(), resultPage.getContent().getFirst().getName());
    }

    @Test
    void getList_success_empty() {
        Page<Person> emptyPersonsPage = new PageImpl<>(Collections.emptyList());
        Page<PersonGetDTO> emptyPersonGetDTOPage = new PageImpl<>(Collections.emptyList());

        when(personCustomRepository.findWithFilters(any(PersonGetFiltersDTO.class), any(Pageable.class))).thenReturn(emptyPersonsPage);

        Page<PersonGetDTO> resultPage = personService.getList(personFilters, Pageable.unpaged());

        assertTrue(resultPage.getContent().isEmpty());
        assertEquals(emptyPersonGetDTOPage.getTotalElements(), resultPage.getTotalElements());
    }
}
