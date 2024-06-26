package com.example.auth.infra.repositories;

import com.example.auth.infra.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByCpf(String cpf);

    Optional<Person> findByEmail(String email);
}
