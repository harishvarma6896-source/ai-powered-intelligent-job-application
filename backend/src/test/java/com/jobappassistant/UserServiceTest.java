package com.jobappassistant;

import com.jobappassistant.dto.RegisterRequest;
import com.jobappassistant.entity.User;
import com.jobappassistant.repository.ActivityLogRepository;
import com.jobappassistant.repository.UserProfileRepository;
import com.jobappassistant.repository.UserRepository;
import com.jobappassistant.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest("test_user", "test@example.com", "password123", "Test User");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");
        
        User savedUser = new User();
        savedUser.setId(100L);
        savedUser.setUsername(request.getUsername());
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword("encoded_password");
        savedUser.setFullName(request.getFullName());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("test_user", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void testVerifyEmail_Success() {
        String token = "verification_token_uuid";
        User user = new User();
        user.setId(50L);
        user.setUsername("john");
        user.setVerificationToken(token);
        user.setStatus("PENDING");

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean verified = userService.verifyEmail(token);

        assertTrue(verified);
        assertEquals("ACTIVE", user.getStatus());
        assertNull(user.getVerificationToken());
        verify(userRepository, times(1)).save(user);
    }
}
