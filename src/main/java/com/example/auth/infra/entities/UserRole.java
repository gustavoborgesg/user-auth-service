package com.example.auth.infra.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"WebUserRoleType\"")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "date_alteration")
    private LocalDateTime dateAlteration;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Builder
    public UserRole(String roleName, LocalDateTime dateAlteration, LocalDateTime dateCreation) {
        this.roleName = roleName;
        this.dateAlteration = dateAlteration;
        this.dateCreation = dateCreation;
    }
}