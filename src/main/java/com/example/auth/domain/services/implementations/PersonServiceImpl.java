package com.example.auth.domain.services.implementations;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonGetDTO;
import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.exceptions.PersonException;
import com.example.auth.domain.services.interfaces.PersonService;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.repositories.PersonRepository;
import com.example.auth.infra.repositories.custom.interfaces.PersonCustomRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonCustomRepository personCustomRepository;
    private final PersonException personException;
    private final ModelMapper modelMapper;

    @Override
    public PersonDTO register(PersonModificationDTO personData) {
        if (!Utils.isStringPresent(personData.getCpf())) {
            personException.exceptionEmptyCpf();
        }

        if (!Utils.isCpfValid(personData.getCpf())) {
            personException.exceptionInvalidCpf();
        }

        personRepository.findByCpf(personData.getCpf())
                .ifPresent((duplicatedCpfUser) -> personException.exceptionDuplicatedCpf());

        if (!Utils.isStringPresent(personData.getEmail())) {
            personException.exceptionEmptyEmail();
        }

        if (!Utils.isEmailValid(personData.getEmail())) {
            personException.exceptionInvalidEmail();
        }

        personRepository.findByEmail(personData.getEmail())
                .ifPresent((duplicatedEmailUser) -> personException.exceptionDuplicatedEmail());

        Person person = Person.builder()
                .name(personData.getName())
                .cpf(personData.getCpf())
                .email(personData.getEmail())
                .dateAlteration(Utils.getDateTimeNowFormatted())
                .dateCreation(Utils.getDateTimeNowFormatted())
                .build();

        try {
            person = personRepository.save(person);
        } catch (Exception exception) {
            personException.exceptionDatabaseError();
        }
        return modelMapper.map(person, PersonDTO.class);
    }

    @Override
    public PersonDTO update(Long id, PersonModificationDTO personData) {
        Person person = personRepository.findById(id)
                .orElseThrow(personException::exceptionNonExistentPerson);

        if (Utils.isStringPresent(personData.getName())) {
            person.setName(personData.getName());
        }
        if (Utils.isStringPresent(personData.getCpf())) {
            if (!Utils.isStringPresent(personData.getCpf())) {
                personException.exceptionEmptyCpf();
            }

            if (!Utils.isCpfValid(personData.getCpf())) {
                personException.exceptionInvalidCpf();
            }

            personRepository.findByCpf(personData.getCpf())
                    .ifPresent((duplicatedCpfUser) -> personException.exceptionDuplicatedCpf());

            person.setCpf(personData.getCpf());
        }
        if (Utils.isStringPresent(personData.getEmail())) {
            if (!Utils.isStringPresent(personData.getEmail())) {
                personException.exceptionEmptyEmail();
            }

            if (!Utils.isEmailValid(personData.getEmail())) {
                personException.exceptionInvalidEmail();
            }

            personRepository.findByEmail(personData.getEmail())
                    .ifPresent((duplicatedEmailUser) -> personException.exceptionDuplicatedEmail());

            person.setEmail(personData.getEmail());
        }
        person.setDateAlteration(Utils.getDateTimeNowFormatted());

        try {
            person = personRepository.save(person);
        } catch (Exception exception) {
            personException.exceptionDatabaseError();
        }
        return modelMapper.map(person, PersonDTO.class);
    }

    @Override
    public PersonDTO get(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(personException::exceptionNonExistentPerson);

        return modelMapper.map(person, PersonDTO.class);
    }

    @Override
    public Page<PersonGetDTO> getList(PersonGetFiltersDTO personFilters, Pageable pageable) {
        Page<Person> personsPage = personCustomRepository.findWithFilters(personFilters, pageable);

        return new PageImpl<>(personsPage.stream()
                .map(person -> modelMapper.map(person, PersonGetDTO.class))
                .collect(Collectors.toList()), pageable, personsPage.getTotalElements());
    }

}
