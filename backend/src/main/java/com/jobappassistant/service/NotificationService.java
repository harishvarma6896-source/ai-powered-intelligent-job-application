package com.jobappassistant.service;

import com.jobappassistant.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Long userId, String message, String type);
    List<Notification> getNotifications(Long userId, boolean unreadOnly);
    void markAsRead(Long userId, Long id);
    void markAllAsRead(Long userId);
    void deleteNotification(Long userId, Long id);
}
