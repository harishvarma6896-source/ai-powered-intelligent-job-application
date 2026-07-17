package com.jobappassistant.service;

import com.jobappassistant.dto.AuthRequest;
import com.jobappassistant.dto.AuthResponse;
import com.jobappassistant.dto.RegisterRequest;
import com.jobappassistant.entity.ActivityLog;
import com.jobappassistant.entity.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    boolean verifyEmail(String token);
    boolean initiateForgotPassword(String email);
    boolean resetPassword(String token, String newPassword);
    User findById(Long id);
    List<User> findAll();
    void deleteUser(Long id);
    void logActivity(User user, String action, String description, String ip);
    List<ActivityLog> getRecentActivities(User user);
    List<ActivityLog> getAllActivities();
    User save(User user);
}
