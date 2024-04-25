package com.example.auth.infra.repositories.custom.interfaces;

import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.infra.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonCustomRepository {
    Page<Person> findWithFilters(PersonGetFiltersDTO personFilters, Pageable pageable);
}
