package com.example.auth.domain.services.interfaces;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.person.PersonGetDTO;
import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.dtos.person.PersonModificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {
    PersonDTO register(PersonModificationDTO personData);

    PersonDTO update(Long id, PersonModificationDTO personData);

    PersonDTO get(Long id);

    Page<PersonGetDTO> getList(PersonGetFiltersDTO personFilters, Pageable pageable);
}
