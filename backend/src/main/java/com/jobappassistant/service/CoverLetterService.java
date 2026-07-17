package com.jobappassistant.service;

import com.jobappassistant.dto.CoverLetterRequest;
import com.jobappassistant.dto.CoverLetterResponse;
import com.jobappassistant.entity.CoverLetter;

import java.util.List;

public interface CoverLetterService {
    CoverLetterResponse generateCoverLetter(Long userId, CoverLetterRequest request);
    CoverLetter getCoverLetter(Long userId, Long id);
    List<CoverLetter> getAllCoverLetters(Long userId);
    void deleteCoverLetter(Long userId, Long id);
}
