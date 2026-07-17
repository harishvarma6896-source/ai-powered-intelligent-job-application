package com.jobappassistant.service;

import com.jobappassistant.dto.FeedbackRequest;
import com.jobappassistant.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback submitFeedback(Long userId, FeedbackRequest request);
    List<Feedback> getAllFeedback();
}
