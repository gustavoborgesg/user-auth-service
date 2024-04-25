package com.example.auth.infra.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"WebPerson\"")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Size(min = 11, max = 11)
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "date_alteration")
    private LocalDateTime dateAlteration;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Builder
    public Person(String name, String cpf, String email, LocalDateTime dateCreation, LocalDateTime dateAlteration) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.dateAlteration = dateAlteration;
        this.dateCreation = dateCreation;
    }
}
