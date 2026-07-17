package com.jobappassistant.service;

import com.jobappassistant.dto.AuthRequest;
import com.jobappassistant.dto.AuthResponse;
import com.jobappassistant.dto.RegisterRequest;
import com.jobappassistant.entity.ActivityLog;
import com.jobappassistant.entity.User;
import com.jobappassistant.entity.UserProfile;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.ActivityLogRepository;
import com.jobappassistant.repository.UserProfileRepository;
import com.jobappassistant.repository.UserRepository;
import com.jobappassistant.security.JwtTokenProvider;
import com.jobappassistant.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        
        // Auto-activate first user or admin
        if (request.getUsername().equalsIgnoreCase("admin")) {
            user.setRole("ROLE_ADMIN");
            user.setStatus("ACTIVE");
        } else {
            user.setRole("ROLE_USER");
            user.setStatus("ACTIVE"); // Auto-activate directly for testing convenience, verification token generated regardless
        }
        
        user.setVerificationToken(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);

        // Bootstrap profile
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profileRepository.save(profile);

        logActivity(savedUser, "REGISTER", "User registered successfully.", "127.0.0.1");

        return savedUser;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        logActivity(user, "LOGIN", "User logged in successfully.", "127.0.0.1");

        return new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    @Override
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));
        
        user.setStatus("ACTIVE");
        user.setVerificationToken(null);
        userRepository.save(user);
        
        logActivity(user, "VERIFY_EMAIL", "Email verified successfully.", "127.0.0.1");
        return true;
    }

    @Override
    public boolean initiateForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with email " + email));
        
        user.setResetPasswordToken(UUID.randomUUID().toString());
        userRepository.save(user);
        
        logActivity(user, "FORGOT_PASSWORD", "Password reset request initiated.", "127.0.0.1");
        return true;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
        
        logActivity(user, "RESET_PASSWORD", "Password reset completed successfully.", "127.0.0.1");
        return true;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void logActivity(User user, String action, String description, String ip) {
        ActivityLog log = new ActivityLog(user, action, description, ip);
        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLog> getRecentActivities(User user) {
        return activityLogRepository.findFirst10ByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    public List<ActivityLog> getAllActivities() {
        return activityLogRepository.findFirst50ByOrderByCreatedAtDesc();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
