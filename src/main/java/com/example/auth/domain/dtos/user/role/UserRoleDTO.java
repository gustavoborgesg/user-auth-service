package com.example.auth.domain.dtos.user.role;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleDTO {
    private Long id;
    private String roleName;
    private LocalDateTime dateAlteration;
    private LocalDateTime dateCreation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleDTO that)) return false;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getRoleName(), that.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoleName());
    }

}
