package com.jobappassistant.controller;

import com.jobappassistant.dto.DashboardDto;
import com.jobappassistant.entity.JobApplication;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin
public class JobApplicationController {

    @Autowired
    private JobApplicationService applicationService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @PostMapping
    public ResponseEntity<JobApplication> createApplication(Authentication authentication, @Valid @RequestBody JobApplication application) {
        return ResponseEntity.ok(applicationService.createApplication(getUserId(authentication), application));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplication(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplication(getUserId(authentication), id));
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllApplications(Authentication authentication) {
        return ResponseEntity.ok(applicationService.getAllApplications(getUserId(authentication)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateApplication(Authentication authentication, @PathVariable Long id, @Valid @RequestBody JobApplication application) {
        return ResponseEntity.ok(applicationService.updateApplication(getUserId(authentication), id, application));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(Authentication authentication, @PathVariable Long id) {
        applicationService.deleteApplication(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Job tracking card deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboardData(Authentication authentication) {
        return ResponseEntity.ok(applicationService.getDashboardData(getUserId(authentication)));
    }
}
