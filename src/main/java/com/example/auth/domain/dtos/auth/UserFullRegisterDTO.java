package com.example.auth.domain.dtos.auth;

import com.example.auth.domain.dtos.person.PersonModificationDTO;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserFullRegisterDTO {
    private Long idOldUser;
    private String username;
    private String password;
    private String roleName;
    private PersonModificationDTO person;
}
