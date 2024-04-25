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
@Table(name = "\"WebUserAuth\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRole role;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "date_alteration")
    private LocalDateTime dateAlteration;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true)
    private Person person;

    @Builder
    public User(String username, String password, UserRole role, Boolean enabled, LocalDateTime dateAlteration, LocalDateTime dateCreation, Person person) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.dateAlteration = dateAlteration;
        this.dateCreation = dateCreation;
        this.person = person;
    }
}
