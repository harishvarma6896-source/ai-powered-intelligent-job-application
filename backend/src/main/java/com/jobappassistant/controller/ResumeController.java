package com.jobappassistant.controller;

import com.jobappassistant.dto.ResumeDto;
import com.jobappassistant.entity.Resume;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @PostMapping
    public ResponseEntity<Resume> createResume(Authentication authentication, @Valid @RequestBody ResumeDto dto) {
        return ResponseEntity.ok(resumeService.createResume(getUserId(authentication), dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getResume(getUserId(authentication), id));
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes(Authentication authentication) {
        return ResponseEntity.ok(resumeService.getAllResumes(getUserId(authentication)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ResumeDto dto) {
        return ResponseEntity.ok(resumeService.updateResume(getUserId(authentication), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(Authentication authentication, @PathVariable Long id) {
        resumeService.deleteResume(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Resume deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate")
    public ResponseEntity<Resume> generateAiResume(Authentication authentication, @RequestParam(value = "templateName", defaultValue = "Modern") String templateName) {
        return ResponseEntity.ok(resumeService.generateAiResume(getUserId(authentication), templateName));
    }

    @PostMapping("/{id}/optimize")
    public ResponseEntity<Resume> optimizeResume(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, String> payload) {
        String jdText = payload.get("jdText");
        return ResponseEntity.ok(resumeService.optimizeResume(getUserId(authentication), id, jdText));
    }
}
