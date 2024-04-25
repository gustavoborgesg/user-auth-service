package com.example.auth.domain.dtos.auth;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDTO {
    private Long idOldUser;
    private String username;
    private String password;
    private String roleName;
    private Long personID;
}
