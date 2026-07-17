package com.jobappassistant.service;

import com.jobappassistant.dto.DashboardDto;
import com.jobappassistant.dto.ProfileDto;
import com.jobappassistant.entity.*;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.JobApplicationRepository;
import com.jobappassistant.repository.ResumeRepository;
import com.jobappassistant.repository.UserProfileRepository;
import com.jobappassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public JobApplication createApplication(Long userId, JobApplication app) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        app.setUser(user);
        JobApplication saved = applicationRepository.save(app);

        userService.logActivity(user, "APPLICATION_CREATE", "Added job tracking card: " + app.getJobTitle() + " at " + app.getCompanyName(), "127.0.0.1");

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplication getApplication(Long userId, Long id) {
        JobApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        if (!app.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to job application");
        }
        return app;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> getAllApplications(Long userId) {
        return applicationRepository.findByUserIdOrderByAppliedDateDesc(userId);
    }

    @Override
    @Transactional
    public JobApplication updateApplication(Long userId, Long id, JobApplication incoming) {
        JobApplication app = getApplication(userId, id);

        String oldStatus = app.getStatus();
        
        app.setCompanyName(incoming.getCompanyName());
        app.setJobTitle(incoming.getJobTitle());
        app.setSalary(incoming.getSalary());
        app.setLocation(incoming.getLocation());
        app.setAppliedDate(incoming.getAppliedDate());
        app.setDeadline(incoming.getDeadline());
        app.setStatus(incoming.getStatus());
        app.setNotes(incoming.getNotes());

        JobApplication saved = applicationRepository.save(app);

        if (!oldStatus.equalsIgnoreCase(incoming.getStatus())) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userService.logActivity(user, "APPLICATION_STATUS_CHANGE", "Moved " + app.getJobTitle() + " at " + app.getCompanyName() + " from " + oldStatus + " to " + incoming.getStatus(), "127.0.0.1");
            }
        }

        return saved;
    }

    @Override
    @Transactional
    public void deleteApplication(Long userId, Long id) {
        JobApplication app = getApplication(userId, id);
        applicationRepository.delete(app);

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "APPLICATION_DELETE", "Deleted job application for " + app.getJobTitle() + " at " + app.getCompanyName(), "127.0.0.1");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDto getDashboardData(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        DashboardDto dto = new DashboardDto();
        dto.setFullName(user.getFullName() != null ? user.getFullName() : user.getUsername());
        
        // 1. Completion & Resume Scores
        dto.setProfileCompletion(profileService.calculateCompletion(profile));
        
        Resume activeResume = resumeRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);
        if (activeResume != null) {
            dto.setResumeScore(activeResume.getAtsScore());
            dto.setAtsScore(activeResume.getAtsScore());
        } else {
            dto.setResumeScore(0);
            dto.setAtsScore(0);
        }

        // 2. Metrics count
        dto.setTotalApplications(applicationRepository.countByUserId(userId));
        dto.setPendingApplications(applicationRepository.countByUserIdAndStatus(userId, "Applied") + 
                                  applicationRepository.countByUserIdAndStatus(userId, "Assessment"));
        dto.setSelectedCount(applicationRepository.countByUserIdAndStatus(userId, "Selected") + 
                             applicationRepository.countByUserIdAndStatus(userId, "Accepted") +
                             applicationRepository.countByUserIdAndStatus(userId, "Offer Received"));
        dto.setRejectedCount(applicationRepository.countByUserIdAndStatus(userId, "Rejected"));
        dto.setInterviewsScheduled(applicationRepository.countByUserIdAndStatus(userId, "Interview"));

        // 3. Recent activity list
        List<ActivityLog> logs = userService.getRecentActivities(user);
        dto.setRecentActivities(logs.stream()
                .map(l -> l.getAction() + ": " + l.getDescription())
                .collect(Collectors.toList()));

        // 4. AI suggestions
        List<String> suggestions = new ArrayList<>();
        if (dto.getProfileCompletion() < 80) {
            suggestions.add("Complete your profile (Skills, Education, Experience) to boost recommendation accuracy.");
        }
        if (activeResume == null) {
            suggestions.add("Generate or upload an active resume to run automated ATS scoring checks.");
        } else if (activeResume.getAtsScore() < 80) {
            suggestions.add("Optimize your active resume using the JD Analyzer to achieve an ATS score above 80%.");
        }
        if (dto.getTotalApplications() == 0) {
            suggestions.add("Add your first job application card to begin tracking your interview calendar.");
        }
        if (dto.getInterviewsScheduled() > 0) {
            suggestions.add("You have interviews scheduled! Head over to the AI Interview Preparation module to generate practice questions.");
        }
        dto.setAiSuggestions(suggestions);

        return dto;
    }
}
