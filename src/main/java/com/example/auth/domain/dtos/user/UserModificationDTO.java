package com.example.auth.domain.dtos.user;

import com.example.auth.domain.dtos.person.PersonModificationDTO;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserModificationDTO {
    private String username;
    private String roleName;
    private Boolean enabled;
    private PersonModificationDTO person;
}
