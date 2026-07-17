package com.jobappassistant.service;

import com.jobappassistant.dto.*;
import com.jobappassistant.entity.*;

public interface ProfileService {
    ProfileDto getProfile(Long userId);
    ProfileDto updateProfile(Long userId, ProfileDto profileDto);
    
    Skill addSkill(Long userId, SkillDto skillDto);
    void deleteSkill(Long userId, Long skillId);
    
    Education addEducation(Long userId, EducationDto educationDto);
    void deleteEducation(Long userId, Long educationId);
    
    Experience addExperience(Long userId, ExperienceDto experienceDto);
    void deleteExperience(Long userId, Long experienceId);
    
    Project addProject(Long userId, ProjectDto projectDto);
    void deleteProject(Long userId, Long projectId);
    
    Certification addCertification(Long userId, CertificationDto certDto);
    void deleteCertification(Long userId, Long certId);
    
    int calculateCompletion(UserProfile profile);
}
