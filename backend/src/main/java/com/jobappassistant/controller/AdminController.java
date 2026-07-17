package com.jobappassistant.controller;

import com.jobappassistant.dto.UserDto;
import com.jobappassistant.entity.ActivityLog;
import com.jobappassistant.entity.User;
import com.jobappassistant.service.FeedbackService;
import com.jobappassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole(), u.getStatus(), u.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<ActivityLog>> getAllActivities() {
        return ResponseEntity.ok(userService.getAllActivities());
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats() {
        List<User> users = userService.findAll();
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(u -> u.getStatus().equalsIgnoreCase("ACTIVE")).count();
        long admins = users.stream().filter(u -> u.getRole().equalsIgnoreCase("ROLE_ADMIN")).count();
        
        double avgFeedbackRating = feedbackService.getAllFeedback().stream()
                .mapToInt(f -> f.getRating())
                .average()
                .orElse(0.0);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("admins", admins);
        stats.put("averageFeedbackRating", Math.round(avgFeedbackRating * 100.0) / 100.0);
        stats.put("totalFeedbackCount", feedbackService.getAllFeedback().size());
        
        return ResponseEntity.ok(stats);
    }
}
