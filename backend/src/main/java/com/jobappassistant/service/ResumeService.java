package com.jobappassistant.service;

import com.jobappassistant.dto.ResumeDto;
import com.jobappassistant.entity.Resume;

import java.util.List;

public interface ResumeService {
    Resume createResume(Long userId, ResumeDto resumeDto);
    Resume getResume(Long userId, Long resumeId);
    List<Resume> getAllResumes(Long userId);
    Resume updateResume(Long userId, Long resumeId, ResumeDto resumeDto);
    void deleteResume(Long userId, Long resumeId);
    Resume generateAiResume(Long userId, String templateName);
    Resume optimizeResume(Long userId, Long resumeId, String jdText);
}
