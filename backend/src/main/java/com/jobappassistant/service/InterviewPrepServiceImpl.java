package com.jobappassistant.service;

import com.jobappassistant.dto.ProfileDto;
import com.jobappassistant.entity.InterviewPrep;
import com.jobappassistant.entity.User;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.InterviewPrepRepository;
import com.jobappassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewPrepServiceImpl implements InterviewPrepService {

    @Autowired
    private InterviewPrepRepository interviewPrepRepository;

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
    public InterviewPrep generateQuestions(Long userId, String jobTitle, String company, String jdText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDto profileDto = profileService.getProfile(userId);
        List<String> skillNames = profileDto.getSkills().stream().map(s -> s.getName()).collect(Collectors.toList());

        String questionsJson = aiService.generateInterviewQuestions(
                profileDto.getBio(),
                skillNames,
                jobTitle,
                company,
                jdText
        );

        InterviewPrep prep = new InterviewPrep(user, jobTitle, company, questionsJson);
        InterviewPrep saved = interviewPrepRepository.save(prep);

        userService.logActivity(user, "INTERVIEW_PREP_GEN", "Generated practice questions for " + jobTitle + " at " + company, "127.0.0.1");

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewPrep> getAllPreps(Long userId) {
        return interviewPrepRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewPrep getPrep(Long userId, Long id) {
        InterviewPrep prep = interviewPrepRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview preparation record not found"));

        if (!prep.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to interview prep");
        }
        return prep;
    }

    @Override
    @Transactional
    public void deletePrep(Long userId, Long id) {
        InterviewPrep prep = getPrep(userId, id);
        interviewPrepRepository.delete(prep);

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "INTERVIEW_PREP_DELETE", "Deleted interview prep for " + prep.getJobTitle() + " at " + prep.getCompany(), "127.0.0.1");
        }
    }
}
