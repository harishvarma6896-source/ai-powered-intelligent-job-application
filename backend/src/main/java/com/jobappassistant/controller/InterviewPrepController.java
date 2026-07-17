package com.jobappassistant.controller;

import com.jobappassistant.entity.InterviewPrep;
import com.jobappassistant.security.UserPrincipal;
import com.jobappassistant.service.InterviewPrepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin
public class InterviewPrepController {

    @Autowired
    private InterviewPrepService interviewPrepService;

    private Long getUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @PostMapping("/generate")
    public ResponseEntity<InterviewPrep> generateQuestions(Authentication authentication, @RequestBody Map<String, String> payload) {
        String jobTitle = payload.get("jobTitle");
        String company = payload.get("company");
        String jdText = payload.get("jdText");
        
        return ResponseEntity.ok(interviewPrepService.generateQuestions(getUserId(authentication), jobTitle, company, jdText));
    }

    @GetMapping
    public ResponseEntity<List<InterviewPrep>> getAllPreps(Authentication authentication) {
        return ResponseEntity.ok(interviewPrepService.getAllPreps(getUserId(authentication)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewPrep> getPrep(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(interviewPrepService.getPrep(getUserId(authentication), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrep(Authentication authentication, @PathVariable Long id) {
        interviewPrepService.deletePrep(getUserId(authentication), id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Interview preparation deleted successfully");
        return ResponseEntity.ok(response);
    }
}
