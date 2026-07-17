package com.jobappassistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_recommendations")
public class JobRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(name = "skills_matched", columnDefinition = "TEXT")
    private String skillsMatched;

    @Column(name = "match_score")
    private Integer matchScore = 0;

    @Column(length = 100)
    private String location;

    @Column(length = 50)
    private String salary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "application_url")
    private String applicationUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public JobRecommendation() {
    }

    public JobRecommendation(User user, String jobTitle, String company, String skillsMatched, Integer matchScore, String location, String salary, String description, String applicationUrl) {
        this.user = user;
        this.jobTitle = jobTitle;
        this.company = company;
        this.skillsMatched = skillsMatched;
        this.matchScore = matchScore;
        this.location = location;
        this.salary = salary;
        this.description = description;
        this.applicationUrl = applicationUrl;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSkillsMatched() {
        return skillsMatched;
    }

    public void setSkillsMatched(String skillsMatched) {
        this.skillsMatched = skillsMatched;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
