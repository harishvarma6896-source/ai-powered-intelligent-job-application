package com.jobappassistant.controller;

import com.jobappassistant.entity.Notification;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication, @RequestParam(value = "unreadOnly", defaultValue = "false") boolean unreadOnly) {
        return ResponseEntity.ok(notificationService.getNotifications(getUserId(authentication), unreadOnly));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(Authentication authentication, @PathVariable Long id) {
        notificationService.markAsRead(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification marked as read");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(Authentication authentication) {
        notificationService.markAllAsRead(getUserId(authentication));
        Map<String, String> response = new HashMap<>();
        response.put("message", "All notifications marked as read");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(Authentication authentication, @PathVariable Long id) {
        notificationService.deleteNotification(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification deleted successfully");
        return ResponseEntity.ok(response);
    }
}
