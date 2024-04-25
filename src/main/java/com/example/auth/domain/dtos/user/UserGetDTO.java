package com.example.auth.domain.dtos.user;

import com.example.auth.domain.dtos.person.PersonGetDTO;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGetDTO {
    private Long id;
    private String username;
    private String roleName;
    private Boolean enabled;
    private PersonGetDTO person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGetDTO that = (UserGetDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(roleName, that.roleName) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, roleName, enabled, person);
    }
}
