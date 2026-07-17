package com.jobappassistant.dto;

import jakarta.validation.constraints.NotBlank;

public class CoverLetterRequest {
    private String recipient;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Job Title is required")
    private String jobTitle;

    @NotBlank(message = "Job Description is required")
    private String jdText;

    private String tone = "Professional"; // Formal, Professional, Friendly, Experienced, Fresher

    public CoverLetterRequest() {
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

    public String getJdText() {
        return jdText;
    }

    public void setJdText(String jdText) {
        this.jdText = jdText;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}
