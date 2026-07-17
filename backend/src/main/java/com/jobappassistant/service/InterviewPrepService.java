package com.jobappassistant.service;

import com.jobappassistant.entity.InterviewPrep;

import java.util.List;

public interface InterviewPrepService {
    InterviewPrep generateQuestions(Long userId, String jobTitle, String company, String jdText);
    List<InterviewPrep> getAllPreps(Long userId);
    InterviewPrep getPrep(Long userId, Long id);
    void deletePrep(Long userId, Long id);
}
