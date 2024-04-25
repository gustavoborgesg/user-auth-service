package com.example.auth.controllers;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonGetDTO;
import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import com.example.auth.domain.services.interfaces.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO registerPerson(@RequestBody PersonModificationDTO personData) {
        return personService.register(personData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PersonDTO updatePerson(@PathVariable Long id, @RequestBody PersonModificationDTO personData) {
        return personService.update(id, personData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO getPerson(@PathVariable Long id) {
        return personService.get(id);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Page<PersonGetDTO> getPersonList(@ModelAttribute PersonGetFiltersDTO filters, Pageable pageable) {
        return personService.getList(filters, pageable);
    }

}
