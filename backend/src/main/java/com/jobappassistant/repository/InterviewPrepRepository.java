package com.jobappassistant.repository;

import com.jobappassistant.entity.InterviewPrep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewPrepRepository extends JpaRepository<InterviewPrep, Long> {
    List<InterviewPrep> findByUserId(Long userId);
}
