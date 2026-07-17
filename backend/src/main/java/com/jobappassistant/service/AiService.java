package com.jobappassistant.service;

import com.jobappassistant.dto.JdAnalysisResponse;
import com.jobappassistant.entity.JobRecommendation;
import com.jobappassistant.entity.User;

import java.util.List;

public interface AiService {
    String generateResumeContent(String profileSummary, List<String> skills, String templateName);
    
    String generateCoverLetter(String resumeSummary, List<String> skills, String recipient, 
                               String company, String jobTitle, String jdText, String tone);
                               
    JdAnalysisResponse analyzeJobDescription(String resumeSummary, List<String> resumeSkills, String jdText);
    
    String generateInterviewQuestions(String resumeSummary, List<String> skills, 
                                      String jobTitle, String company, String jdText);
                                      
    List<JobRecommendation> generateJobRecommendations(User user, List<String> userSkills, String location, String title);
}
