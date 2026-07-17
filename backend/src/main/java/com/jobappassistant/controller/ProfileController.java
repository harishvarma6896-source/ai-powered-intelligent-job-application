package com.jobappassistant.controller;

import com.jobappassistant.dto.*;
import com.jobappassistant.entity.*;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfile(getUserId(authentication)));
    }

    @PutMapping
    public ResponseEntity<ProfileDto> updateProfile(Authentication authentication, @Valid @RequestBody ProfileDto profileDto) {
        return ResponseEntity.ok(profileService.updateProfile(getUserId(authentication), profileDto));
    }

    @PostMapping("/skills")
    public ResponseEntity<?> addSkill(Authentication authentication, @Valid @RequestBody SkillDto skillDto) {
        Skill skill = profileService.addSkill(getUserId(authentication), skillDto);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<?> deleteSkill(Authentication authentication, @PathVariable Long id) {
        profileService.deleteSkill(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Skill deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/education")
    public ResponseEntity<?> addEducation(Authentication authentication, @Valid @RequestBody EducationDto educationDto) {
        Education edu = profileService.addEducation(getUserId(authentication), educationDto);
        return ResponseEntity.ok(edu);
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteEducation(Authentication authentication, @PathVariable Long id) {
        profileService.deleteEducation(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Education record deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/experience")
    public ResponseEntity<?> addExperience(Authentication authentication, @Valid @RequestBody ExperienceDto experienceDto) {
        Experience exp = profileService.addExperience(getUserId(authentication), experienceDto);
        return ResponseEntity.ok(exp);
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<?> deleteExperience(Authentication authentication, @PathVariable Long id) {
        profileService.deleteExperience(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Experience record deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/projects")
    public ResponseEntity<?> addProject(Authentication authentication, @Valid @RequestBody ProjectDto projectDto) {
        Project proj = profileService.addProject(getUserId(authentication), projectDto);
        return ResponseEntity.ok(proj);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(Authentication authentication, @PathVariable Long id) {
        profileService.deleteProject(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Project deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/certifications")
    public ResponseEntity<?> addCertification(Authentication authentication, @Valid @RequestBody CertificationDto certDto) {
        Certification cert = profileService.addCertification(getUserId(authentication), certDto);
        return ResponseEntity.ok(cert);
    }

    @DeleteMapping("/certifications/{id}")
    public ResponseEntity<?> deleteCertification(Authentication authentication, @PathVariable Long id) {
        profileService.deleteCertification(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Certification deleted successfully");
        return ResponseEntity.ok(response);
    }
}
