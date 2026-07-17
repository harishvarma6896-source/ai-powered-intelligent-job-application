package com.jobappassistant.controller;

import com.jobappassistant.dto.CoverLetterRequest;
import com.jobappassistant.dto.CoverLetterResponse;
import com.jobappassistant.entity.CoverLetter;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.CoverLetterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cover-letters")
@CrossOrigin
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @PostMapping("/generate")
    public ResponseEntity<CoverLetterResponse> generateCoverLetter(Authentication authentication, @Valid @RequestBody CoverLetterRequest request) {
        return ResponseEntity.ok(coverLetterService.generateCoverLetter(getUserId(authentication), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoverLetter> getCoverLetter(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(coverLetterService.getCoverLetter(getUserId(authentication), id));
    }

    @GetMapping
    public ResponseEntity<List<CoverLetter>> getAllCoverLetters(Authentication authentication) {
        return ResponseEntity.ok(coverLetterService.getAllCoverLetters(getUserId(authentication)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoverLetter(Authentication authentication, @PathVariable Long id) {
        coverLetterService.deleteCoverLetter(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cover letter deleted successfully");
        return ResponseEntity.ok(response);
    }
}
