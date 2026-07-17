package com.jobappassistant.dto;

import java.util.ArrayList;
import java.util.List;

public class JdAnalysisResponse {
    private Integer atsScore;
    private Integer matchPercentage;
    private List<String> missingSkills = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();
    private List<String> recommendedCertifications = new ArrayList<>();
    private List<String> extractedSkills = new ArrayList<>();
    private String extractedExperience;
    private String extractedEducation;
    private String extractedSalary;

    public JdAnalysisResponse() {
    }

    // Getters and Setters
    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public Integer getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(Integer matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getRecommendedCertifications() {
        return recommendedCertifications;
    }

    public void setRecommendedCertifications(List<String> recommendedCertifications) {
        this.recommendedCertifications = recommendedCertifications;
    }

    public List<String> getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(List<String> extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public String getExtractedExperience() {
        return extractedExperience;
    }

    public void setExtractedExperience(String extractedExperience) {
        this.extractedExperience = extractedExperience;
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
}
