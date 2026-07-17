package com.jobappassistant.controller;

import com.jobappassistant.dto.AuthRequest;
import com.jobappassistant.dto.AuthResponse;
import com.jobappassistant.dto.RegisterRequest;
import com.jobappassistant.entity.User;
import com.jobappassistant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.register(registerRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully! Check your inbox for verification details.");
        response.put("username", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = userService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        userService.verifyEmail(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verification successful! You can now log in.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        userService.initiateForgotPassword(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset instructions have been emailed to you.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword) {
        userService.resetPassword(token, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Your password has been reset successfully! You can now log in.");
        return ResponseEntity.ok(response);
    }
}
