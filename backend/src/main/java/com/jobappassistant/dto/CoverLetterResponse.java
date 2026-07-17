package com.jobappassistant.dto;

import java.time.LocalDateTime;

public class CoverLetterResponse {
    private Long id;
    private String recipient;
    private String company;
    private String jobTitle;
    private String tone;
    private String content;
    private LocalDateTime createdAt;

    public CoverLetterResponse() {
    }

    public CoverLetterResponse(Long id, String recipient, String company, String jobTitle, String tone, String content, LocalDateTime createdAt) {
        this.id = id;
        this.recipient = recipient;
        this.company = company;
        this.jobTitle = jobTitle;
        this.tone = tone;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
