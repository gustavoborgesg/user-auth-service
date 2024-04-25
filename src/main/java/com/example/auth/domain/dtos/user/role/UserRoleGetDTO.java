package com.example.auth.domain.dtos.user.role;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleGetDTO {
    private Long id;
    private String roleName;
}