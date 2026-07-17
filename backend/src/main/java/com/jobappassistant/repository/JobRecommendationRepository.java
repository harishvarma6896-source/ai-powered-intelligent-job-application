package com.jobappassistant.repository;

import com.jobappassistant.entity.JobRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {
    List<JobRecommendation> findByUserId(Long userId);
    List<JobRecommendation> findByUserIdOrderByMatchScoreDesc(Long userId);
}
