package com.jobappassistant.dto;

public class ResumeDto {
    private Long id;
    private String title;
    private String contentJson;
    private String filePath;
    private Integer atsScore;
    private String improvementSuggestions;
    private String templateName;
    private Integer version;
    private Boolean isActive;

    public ResumeDto() {
    }

    public ResumeDto(Long id, String title, String contentJson, String filePath, Integer atsScore, String improvementSuggestions, String templateName, Integer version, Boolean isActive) {
        this.id = id;
        this.title = title;
        this.contentJson = contentJson;
        this.filePath = filePath;
        this.atsScore = atsScore;
        this.improvementSuggestions = improvementSuggestions;
        this.templateName = templateName;
        this.version = version;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public String getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(String improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
