package com.jobappassistant.service;

import com.jobappassistant.dto.FeedbackRequest;
import com.jobappassistant.entity.Feedback;
import com.jobappassistant.entity.User;
import com.jobappassistant.repository.FeedbackRepository;
import com.jobappassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Feedback submitFeedback(Long userId, FeedbackRequest request) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        Feedback feedback = new Feedback(
                user,
                request.getEmail(),
                request.getRating(),
                request.getComment()
        );

        return feedbackRepository.save(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findByOrderByCreatedAtDesc();
    }
}
