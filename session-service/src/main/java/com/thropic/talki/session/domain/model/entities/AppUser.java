package com.thropic.talki.session.domain.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(name = "academic_segment")
    private String academicSegment;

    protected AppUser() {}

    public AppUser(String email, String username, String academicSegment) {
        this.email = email;
        this.username = username;
        this.academicSegment = academicSegment;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getAcademicSegment() { return academicSegment; }
}
