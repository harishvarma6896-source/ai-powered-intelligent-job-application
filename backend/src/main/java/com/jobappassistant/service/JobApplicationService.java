package com.jobappassistant.service;

import com.jobappassistant.dto.DashboardDto;
import com.jobappassistant.entity.JobApplication;

import java.util.List;

public interface JobApplicationService {
    JobApplication createApplication(Long userId, JobApplication application);
    JobApplication getApplication(Long userId, Long id);
    List<JobApplication> getAllApplications(Long userId);
    JobApplication updateApplication(Long userId, Long id, JobApplication application);
    void deleteApplication(Long userId, Long id);
    DashboardDto getDashboardData(Long userId);
}
