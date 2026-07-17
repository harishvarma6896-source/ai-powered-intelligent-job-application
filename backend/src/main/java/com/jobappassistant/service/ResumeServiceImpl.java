package com.jobappassistant.service;

import com.jobappassistant.dto.JdAnalysisResponse;
import com.jobappassistant.dto.ProfileDto;
import com.jobappassistant.dto.ResumeDto;
import com.jobappassistant.entity.Resume;
import com.jobappassistant.entity.User;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.ResumeRepository;
import com.jobappassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

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
    public Resume createResume(Long userId, ResumeDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getIsActive()) {
            deactivateAllResumes(userId);
        }

        Resume resume = new Resume(
                user,
                dto.getTitle(),
                dto.getContentJson(),
                dto.getAtsScore() != null ? dto.getAtsScore() : 75,
                dto.getImprovementSuggestions() != null ? dto.getImprovementSuggestions() : "Add quantified achievements.",
                dto.getTemplateName() != null ? dto.getTemplateName() : "Modern",
                dto.getVersion() != null ? dto.getVersion() : 1
        );
        resume.setIsActive(dto.getIsActive());
        resume.setFilePath(dto.getFilePath());

        Resume saved = resumeRepository.save(savedResumeLog(user, resume));
        return saved;
    }

    private Resume savedResumeLog(User user, Resume resume) {
        userService.logActivity(user, "RESUME_CREATE", "Created resume: " + resume.getTitle(), "127.0.0.1");
        return resume;
    }

    private void deactivateAllResumes(Long userId) {
        List<Resume> activeResumes = resumeRepository.findByUserId(userId);
        for (Resume r : activeResumes) {
            r.setIsActive(false);
            resumeRepository.save(r);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Resume getResume(Long userId, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to resume");
        }
        return resume;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resume> getAllResumes(Long userId) {
        return resumeRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Resume updateResume(Long userId, Long resumeId, ResumeDto dto) {
        Resume resume = getResume(userId, resumeId);
        
        resume.setTitle(dto.getTitle());
        resume.setContentJson(dto.getContentJson());
        resume.setTemplateName(dto.getTemplateName());
        
        if (dto.getAtsScore() != null) resume.setAtsScore(dto.getAtsScore());
        if (dto.getImprovementSuggestions() != null) resume.setImprovementSuggestions(dto.getImprovementSuggestions());
        
        if (dto.getIsActive() && !resume.getIsActive()) {
            deactivateAllResumes(userId);
            resume.setIsActive(true);
        } else if (!dto.getIsActive()) {
            resume.setIsActive(false);
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "RESUME_UPDATE", "Updated resume: " + resume.getTitle(), "127.0.0.1");
        }

        return resumeRepository.save(resume);
    }

    @Override
    @Transactional
    public void deleteResume(Long userId, Long resumeId) {
        Resume resume = getResume(userId, resumeId);
        resumeRepository.delete(resume);
        
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "RESUME_DELETE", "Deleted resume: " + resume.getTitle(), "127.0.0.1");
        }
    }

    @Override
    @Transactional
    public Resume generateAiResume(Long userId, String templateName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDto profileDto = profileService.getProfile(userId);
        List<String> skillNames = profileDto.getSkills().stream().map(s -> s.getName()).collect(Collectors.toList());

        String generatedContent = aiService.generateResumeContent(profileDto.getBio(), skillNames, templateName);

        deactivateAllResumes(userId);

        Resume resume = new Resume(
                user,
                "AI Optimized Resume (" + templateName + ")",
                "{\"markdownContent\":\"" + generatedContent.replace("\"", "\\\"").replace("\n", "\\n") + "\"}",
                85,
                "Add certification tags.\nInclude link to personal portfolio site.",
                templateName,
                1
        );
        resume.setIsActive(true);

        userService.logActivity(user, "RESUME_AI_GEN", "Generated AI-powered resume.", "127.0.0.1");

        return resumeRepository.save(resume);
    }

    @Override
    @Transactional
    public Resume optimizeResume(Long userId, Long resumeId, String jdText) {
        Resume resume = getResume(userId, resumeId);
        ProfileDto profileDto = profileService.getProfile(userId);
        List<String> skillNames = profileDto.getSkills().stream().map(s -> s.getName()).collect(Collectors.toList());

        JdAnalysisResponse analysis = aiService.analyzeJobDescription(profileDto.getBio(), skillNames, jdText);

        resume.setAtsScore(analysis.getAtsScore());
        resume.setImprovementSuggestions(String.join("\n", analysis.getSuggestions()));
        
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "RESUME_OPTIMIZE", "Optimized resume against job description. ATS Score: " + analysis.getAtsScore(), "127.0.0.1");
        }

        return resumeRepository.save(resume);
    }
}
