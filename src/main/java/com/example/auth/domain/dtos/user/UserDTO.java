package com.example.auth.domain.dtos.user;

import com.example.auth.domain.dtos.person.PersonDTO;
import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private UserRoleDTO role;
    private Boolean enabled;
    private LocalDateTime dateAlteration;
    private LocalDateTime dateCreation;
    private PersonDTO person;
}
