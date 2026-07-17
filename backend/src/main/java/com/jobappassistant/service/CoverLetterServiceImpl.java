package com.jobappassistant.service;

import com.jobappassistant.dto.CoverLetterRequest;
import com.jobappassistant.dto.CoverLetterResponse;
import com.jobappassistant.dto.ProfileDto;
import com.jobappassistant.entity.CoverLetter;
import com.jobappassistant.entity.User;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.CoverLetterRepository;
import com.jobappassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoverLetterServiceImpl implements CoverLetterService {

    @Autowired
    private CoverLetterRepository coverLetterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AiService aiService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public CoverLetterResponse generateCoverLetter(Long userId, CoverLetterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDto profileDto = profileService.getProfile(userId);
        List<String> skillNames = profileDto.getSkills().stream().map(s -> s.getName()).collect(Collectors.toList());

        String generatedLetter = aiService.generateCoverLetter(
                profileDto.getBio(),
                skillNames,
                request.getRecipient(),
                request.getCompany(),
                request.getJobTitle(),
                request.getJdText(),
                request.getTone()
        );

        CoverLetter coverLetter = new CoverLetter(
                user,
                request.getRecipient(),
                request.getCompany(),
                request.getJobTitle(),
                request.getTone(),
                generatedLetter
        );

        CoverLetter saved = coverLetterRepository.save(coverLetter);

        userService.logActivity(user, "COVER_LETTER_GEN", "Generated cover letter for " + request.getJobTitle() + " at " + request.getCompany(), "127.0.0.1");

        return new CoverLetterResponse(
                saved.getId(),
                saved.getRecipient(),
                saved.getCompany(),
                saved.getJobTitle(),
                saved.getTone(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CoverLetter getCoverLetter(Long userId, Long id) {
        CoverLetter cl = coverLetterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cover letter not found"));

        if (!cl.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to cover letter");
        }
        return cl;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoverLetter> getAllCoverLetters(Long userId) {
        return coverLetterRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteCoverLetter(Long userId, Long id) {
        CoverLetter cl = getCoverLetter(userId, id);
        coverLetterRepository.delete(cl);
        
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "COVER_LETTER_DELETE", "Deleted cover letter for " + cl.getJobTitle() + " at " + cl.getCompany(), "127.0.0.1");
        }
    }
}
