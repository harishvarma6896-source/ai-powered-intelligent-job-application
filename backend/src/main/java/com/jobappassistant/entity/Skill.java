package com.jobappassistant.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills", uniqueConstraints = {@UniqueConstraint(columnNames = {"profile_id", "name"})})
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile profile;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20)
    private String proficiency = "Intermediate"; // Beginner, Intermediate, Advanced, Expert

    // Constructors
    public Skill() {
    }

    public Skill(UserProfile profile, String name, String proficiency) {
        this.profile = profile;
        this.name = name;
        this.proficiency = proficiency;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
}
