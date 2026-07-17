package com.jobappassistant.dto;

import java.util.ArrayList;
import java.util.List;

public class DashboardDto {
    private String fullName;
    private Integer profileCompletion = 0;
    private Integer resumeScore = 0;
    private Integer atsScore = 0;
    private Long totalApplications = 0L;
    private Long pendingApplications = 0L;
    private Long selectedCount = 0L;
    private Long rejectedCount = 0L;
    private Long interviewsScheduled = 0L;
    private List<String> aiSuggestions = new ArrayList<>();
    private List<String> recentActivities = new ArrayList<>();

    public DashboardDto() {
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getProfileCompletion() {
        return profileCompletion;
    }

    public void setProfileCompletion(Integer profileCompletion) {
        this.profileCompletion = profileCompletion;
    }

    public Integer getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(Integer resumeScore) {
        this.resumeScore = resumeScore;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Long getPendingApplications() {
        return pendingApplications;
    }

    public void setPendingApplications(Long pendingApplications) {
        this.pendingApplications = pendingApplications;
    }

    public Long getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(Long selectedCount) {
        this.selectedCount = selectedCount;
    }

    public Long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(Long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public Long getInterviewsScheduled() {
        return interviewsScheduled;
    }

    public void setInterviewsScheduled(Long interviewsScheduled) {
        this.interviewsScheduled = interviewsScheduled;
    }

    public List<String> getAiSuggestions() {
        return aiSuggestions;
    }

    public void setAiSuggestions(List<String> aiSuggestions) {
        this.aiSuggestions = aiSuggestions;
    }

    public List<String> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<String> recentActivities) {
        this.recentActivities = recentActivities;
    }
}
