package com.jobappassistant.controller;

import com.jobappassistant.dto.FeedbackRequest;
import com.jobappassistant.entity.Feedback;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(Authentication authentication, @Valid @RequestBody FeedbackRequest request) {
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            userId = principal.getId();
        }

        Feedback feedback = feedbackService.submitFeedback(userId, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Thank you! Your feedback has been recorded successfully.");
        response.put("feedbackId", feedback.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }
}
