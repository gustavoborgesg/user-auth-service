package com.example.auth.domain.dtos.person;

import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonGetDTO {
    private Long id;
    private String name;
    private String cpf;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonGetDTO that = (PersonGetDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(cpf, that.cpf) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cpf, email);
    }
}
