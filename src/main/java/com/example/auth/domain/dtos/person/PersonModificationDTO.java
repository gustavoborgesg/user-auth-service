package com.example.auth.domain.dtos.person;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonModificationDTO {
    private String name;
    private String cpf;
    private String email;
}
