package com.jobappassistant.repository;

import com.jobappassistant.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ActivityLog> findFirst10ByUserIdOrderByCreatedAtDesc(Long userId);
    List<ActivityLog> findFirst50ByOrderByCreatedAtDesc();
}
