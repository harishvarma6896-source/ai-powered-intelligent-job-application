package com.jobappassistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
public class JobDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(name = "jd_text", columnDefinition = "LONGTEXT", nullable = false)
    private String jdText;

    @Column(name = "extracted_skills", columnDefinition = "TEXT")
    private String extractedSkills;

    @Column(name = "extracted_experience", length = 100)
    private String extractedExperience;

    @Column(name = "extracted_keywords", columnDefinition = "TEXT")
    private String extractedKeywords;

    @Column(name = "extracted_responsibilities", columnDefinition = "TEXT")
    private String extractedResponsibilities;

    @Column(name = "extracted_education", length = 100)
    private String extractedEducation;

    @Column(name = "extracted_salary", length = 100)
    private String extractedSalary;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public JobDescription() {
    }

    public JobDescription(User user, String title, String company, String jdText) {
        this.user = user;
        this.title = title;
        this.company = company;
        this.jdText = jdText;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJdText() {
        return jdText;
    }

    public void setJdText(String jdText) {
        this.jdText = jdText;
    }

    public String getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(String extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public String getExtractedExperience() {
        return extractedExperience;
    }

    public void setExtractedExperience(String extractedExperience) {
        this.extractedExperience = extractedExperience;
    }

    public String getExtractedKeywords() {
        return extractedKeywords;
    }

    public void setExtractedKeywords(String extractedKeywords) {
        this.extractedKeywords = extractedKeywords;
    }

    public String getExtractedResponsibilities() {
        return extractedResponsibilities;
    }

    public void setExtractedResponsibilities(String extractedResponsibilities) {
        this.extractedResponsibilities = extractedResponsibilities;
    }

    public String getExtractedEducation() {
        return extractedEducation;
    }

    public void setExtractedEducation(String extractedEducation) {
        this.extractedEducation = extractedEducation;
    }

    public String getExtractedSalary() {
        return extractedSalary;
    }

    public void setExtractedSalary(String extractedSalary) {
        this.extractedSalary = extractedSalary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
