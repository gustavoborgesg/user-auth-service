package com.example.auth.domain.dtos.user;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGetListFiltersDTO {
    private String username;
    private Boolean enabled;

    //UserRole
    private String roleName;

    //Person
    private String name;
    private String cpf;
    private String email;
}
