package com.jobappassistant.repository;

import com.jobappassistant.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserId(Long userId);
    List<JobApplication> findByUserIdOrderByAppliedDateDesc(Long userId);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, String status);
}
