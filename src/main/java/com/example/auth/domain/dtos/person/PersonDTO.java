package com.example.auth.domain.dtos.person;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private LocalDateTime dateAlteration;
    private LocalDateTime dateCreation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO that = (PersonDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(cpf, that.cpf) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cpf, email);
    }
}
