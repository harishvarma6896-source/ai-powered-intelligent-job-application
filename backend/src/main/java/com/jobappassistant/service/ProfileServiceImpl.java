package com.jobappassistant.service;

import com.jobappassistant.dto.*;
import com.jobappassistant.entity.*;
import com.jobappassistant.exception.BadRequestException;
import com.jobappassistant.exception.ResourceNotFoundException;
import com.jobappassistant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getProfile(Long userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

        ProfileDto dto = new ProfileDto();
        dto.setTitle(profile.getTitle());
        dto.setBio(profile.getBio());
        dto.setPhone(profile.getPhone());
        dto.setLocation(profile.getLocation());
        dto.setProfilePicUrl(profile.getProfilePicUrl());
        dto.setPortfolioUrl(profile.getPortfolioUrl());
        dto.setLinkedinUrl(profile.getLinkedinUrl());
        dto.setGithubUrl(profile.getGithubUrl());
        dto.setResumeUrl(profile.getResumeUrl());

        dto.setSkills(skillRepository.findByProfileId(profile.getId()).stream()
                .map(s -> new SkillDto(s.getId(), s.getName(), s.getProficiency()))
                .collect(Collectors.toList()));

        dto.setEducation(educationRepository.findByProfileId(profile.getId()).stream()
                .map(e -> new EducationDto(e.getId(), e.getSchool(), e.getDegree(), e.getFieldOfStudy(), e.getStartDate(), e.getEndDate(), e.getDescription()))
                .collect(Collectors.toList()));

        dto.setExperience(experienceRepository.findByProfileId(profile.getId()).stream()
                .map(exp -> new ExperienceDto(exp.getId(), exp.getCompany(), exp.getPosition(), exp.getLocation(), exp.getStartDate(), exp.getEndDate(), exp.getDescription(), exp.getCurrentlyWorking()))
                .collect(Collectors.toList()));

        dto.setProjects(projectRepository.findByProfileId(profile.getId()).stream()
                .map(p -> new ProjectDto(p.getId(), p.getTitle(), p.getDescription(), p.getTechnologies(), p.getLink()))
                .collect(Collectors.toList()));

        dto.setCertifications(certificationRepository.findByProfileId(profile.getId()).stream()
                .map(c -> new CertificationDto(c.getId(), c.getName(), c.getIssuingOrganization(), c.getIssueDate(), c.getExpirationDate(), c.getCredentialId(), c.getCredentialUrl()))
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    @Transactional
    public ProfileDto updateProfile(Long userId, ProfileDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profile.setTitle(dto.getTitle());
        profile.setBio(dto.getBio());
        profile.setPhone(dto.getPhone());
        profile.setLocation(dto.getLocation());
        profile.setPortfolioUrl(dto.getPortfolioUrl());
        profile.setLinkedinUrl(dto.getLinkedinUrl());
        profile.setGithubUrl(dto.getGithubUrl());
        
        if (dto.getProfilePicUrl() != null) {
            profile.setProfilePicUrl(dto.getProfilePicUrl());
        }
        if (dto.getResumeUrl() != null) {
            profile.setResumeUrl(dto.getResumeUrl());
        }

        profileRepository.save(profile);
        
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userService.logActivity(user, "PROFILE_UPDATE", "Updated core profile configurations.", "127.0.0.1");
        }

        return getProfile(userId);
    }

    @Override
    @Transactional
    public Skill addSkill(Long userId, SkillDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<Skill> existing = skillRepository.findByProfileId(profile.getId());
        boolean alreadyExists = existing.stream().anyMatch(s -> s.getName().equalsIgnoreCase(dto.getName()));
        if (alreadyExists) {
            throw new BadRequestException("Skill already exists on profile");
        }

        Skill skill = new Skill(profile, dto.getName(), dto.getProficiency());
        return skillRepository.save(skill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long userId, Long skillId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (!skill.getProfile().getId().equals(profile.getId())) {
            throw new BadRequestException("Unauthorized skill deletion request");
        }

        skillRepository.delete(skill);
    }

    @Override
    @Transactional
    public Education addEducation(Long userId, EducationDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Education edu = new Education(
                profile,
                dto.getSchool(),
                dto.getDegree(),
                dto.getFieldOfStudy(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDescription()
        );
        return educationRepository.save(edu);
    }

    @Override
    @Transactional
    public void deleteEducation(Long userId, Long id) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Education edu = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found"));

        if (!edu.getProfile().getId().equals(profile.getId())) {
            throw new BadRequestException("Unauthorized education deletion request");
        }

        educationRepository.delete(edu);
    }

    @Override
    @Transactional
    public Experience addExperience(Long userId, ExperienceDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Experience exp = new Experience(
                profile,
                dto.getCompany(),
                dto.getPosition(),
                dto.getLocation(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDescription(),
                dto.getCurrentlyWorking()
        );
        return experienceRepository.save(exp);
    }

    @Override
    @Transactional
    public void deleteExperience(Long userId, Long id) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Experience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience record not found"));

        if (!exp.getProfile().getId().equals(profile.getId())) {
            throw new BadRequestException("Unauthorized experience deletion request");
        }

        experienceRepository.delete(exp);
    }

    @Override
    @Transactional
    public Project addProject(Long userId, ProjectDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Project p = new Project(
                profile,
                dto.getTitle(),
                dto.getDescription(),
                dto.getTechnologies(),
                dto.getLink()
        );
        return projectRepository.save(p);
    }

    @Override
    @Transactional
    public void deleteProject(Long userId, Long id) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!p.getProfile().getId().equals(profile.getId())) {
            throw new BadRequestException("Unauthorized project deletion request");
        }

        projectRepository.delete(p);
    }

    @Override
    @Transactional
    public Certification addCertification(Long userId, CertificationDto dto) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Certification cert = new Certification(
                profile,
                dto.getName(),
                dto.getIssuingOrganization(),
                dto.getIssueDate(),
                dto.getExpirationDate(),
                dto.getCredentialId(),
                dto.getCredentialUrl()
        );
        return certificationRepository.save(cert);
    }

    @Override
    @Transactional
    public void deleteCertification(Long userId, Long id) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Certification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

        if (!cert.getProfile().getId().equals(profile.getId())) {
            throw new BadRequestException("Unauthorized certification deletion request");
        }

        certificationRepository.delete(cert);
    }

    @Override
    public int calculateCompletion(UserProfile profile) {
        int score = 0;
        if (profile.getTitle() != null && !profile.getTitle().trim().isEmpty()) score += 10;
        if (profile.getBio() != null && !profile.getBio().trim().isEmpty()) score += 15;
        if (profile.getLocation() != null && !profile.getLocation().trim().isEmpty()) score += 5;
        if (profile.getPhone() != null && !profile.getPhone().trim().isEmpty()) score += 5;

        List<Skill> skills = skillRepository.findByProfileId(profile.getId());
        if (!skills.isEmpty()) score += 20;

        List<Experience> exp = experienceRepository.findByProfileId(profile.getId());
        if (!exp.isEmpty()) score += 20;

        List<Education> edu = educationRepository.findByProfileId(profile.getId());
        if (!edu.isEmpty()) score += 15;

        List<Project> projs = projectRepository.findByProfileId(profile.getId());
        List<Certification> certs = certificationRepository.findByProfileId(profile.getId());
        if (!projs.isEmpty() || !certs.isEmpty()) score += 10;

        return score;
    }
}
