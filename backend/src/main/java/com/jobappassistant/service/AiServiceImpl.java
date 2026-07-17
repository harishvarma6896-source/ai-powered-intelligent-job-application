package com.jobappassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobappassistant.dto.JdAnalysisResponse;
import com.jobappassistant.entity.JobRecommendation;
import com.jobappassistant.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiServiceImpl implements AiService {
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

    @Value("${app.geminiApiKey}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private String queryGemini(String prompt) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty() || geminiApiKey.contains("GEMINI_API_KEY")) {
            logger.info("Gemini API key is not configured, running in local simulated AI mode.");
            return null;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String escapedPrompt = escapeJson(prompt);
            String requestJson = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";
            
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            String url = GEMINI_API_URL + geminiApiKey;
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("content").path("parts").get(0).path("text").asText();
            }
        } catch (Exception e) {
            logger.error("Gemini API call failed, falling back to simulated mode: " + e.getMessage());
        }
        return null;
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public String generateResumeContent(String profileSummary, List<String> skills, String templateName) {
        String prompt = "You are an expert resume writer. Generate an optimized, professional resume content in markdown based on the profile summary: '" 
                + profileSummary + "' and skills: " + String.join(", ", skills) + ". Use template style: " + templateName;

        String result = queryGemini(prompt);
        if (result != null) return result;

        // Mock Fallback
        return "# RESUME - OPTIMIZED\n\n" +
                "## PROFESSIONAL SUMMARY\n" +
                (profileSummary != null && !profileSummary.isEmpty() ? profileSummary : "Result-driven Software Engineer with extensive experience developing full stack web applications.") +
                " Proven track record in microservices architecture, API optimization, and responsive design.\n\n" +
                "## CORE STRENGTHS & SKILLS\n" +
                "- Development languages: " + (skills.isEmpty() ? "Java, JavaScript, SQL" : String.join(", ", skills)) + "\n" +
                "- Frameworks & Tools: Spring Boot, Spring Security, Hibernate, React, Git, Docker\n" +
                "- Best Practices: SOLID design principles, Agile methodologies, RESTful APIs, OOP\n\n" +
                "## SELECTED EXPERIENCE\n" +
                "### Senior Software Engineer | TechCorp Inc\n" +
                "- Developed and designed clean REST APIs handling 5,000+ operations/min using Java and Spring Boot.\n" +
                "- Integrated frontend interfaces using React, boosting user interaction speeds by 25%.\n" +
                "- Authored automated unit test suites covering 90% of business logic paths.\n\n" +
                "## EDUCATION\n" +
                "### B.S. in Computer Science | Global Tech University\n" +
                "GPA: 3.8/4.0 | Completed specialization in Systems Architectures.";
    }

    @Override
    public String generateCoverLetter(String resumeSummary, List<String> skills, String recipient, 
                                       String company, String jobTitle, String jdText, String tone) {
        String prompt = String.format("Generate a cover letter in a %s tone. Recipient: %s, Company: %s, Job Title: %s. " +
                "Resume Summary: %s, Skills: %s. Job description context: %s.",
                tone, recipient, company, jobTitle, resumeSummary, String.join(", ", skills), jdText);

        String result = queryGemini(prompt);
        if (result != null) return result;

        // Mock Fallback
        String salutation = (recipient == null || recipient.isEmpty()) ? "Hiring Manager" : recipient;
        return String.format("Dear %s,\n\n" +
                "I am writing to express my strong interest in the %s position at %s. With my background in software engineering, " +
                "and hands-on expertise in %s, I am confident that I can make a meaningful impact on your engineering group.\n\n" +
                "Throughout my career, I have focused on writing highly optimized code and designing clean, reusable backend and frontend components. " +
                "My experience aligns closely with the requirements outlined in your job posting, specifically regarding system optimization, modern design paradigms, and responsive interfaces.\n\n" +
                "I would love the opportunity to discuss how my skill set and experiences align with your team's goals. Thank you for your time and consideration.\n\n" +
                "Sincerely,\n[Your Name]",
                salutation, jobTitle, company, skills.isEmpty() ? "software architectures" : String.join(", ", skills));
    }

    @Override
    public JdAnalysisResponse analyzeJobDescription(String resumeSummary, List<String> resumeSkills, String jdText) {
        String prompt = "Compare this resume context (Summary: '" + resumeSummary + "', Skills: " + resumeSkills + ") " +
                "with the following Job Description: '" + jdText + "'. Output JSON only in format: " +
                "{\"atsScore\": 85, \"matchPercentage\": 80, \"missingSkills\": [\"AWS\",\"Docker\"], \"suggestions\": [\"Add metrics\"], \"recommendedCertifications\": [\"AWS Architect\"]}";

        String result = queryGemini(prompt);
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(result);
                JdAnalysisResponse response = new JdAnalysisResponse();
                response.setAtsScore(root.path("atsScore").asInt(70));
                response.setMatchPercentage(root.path("matchPercentage").asInt(70));
                
                List<String> missing = new ArrayList<>();
                root.path("missingSkills").forEach(n -> missing.add(n.asText()));
                response.setMissingSkills(missing);

                List<String> sug = new ArrayList<>();
                root.path("suggestions").forEach(n -> sug.add(n.asText()));
                response.setSuggestions(sug);

                List<String> certs = new ArrayList<>();
                root.path("recommendedCertifications").forEach(n -> certs.add(n.asText()));
                response.setRecommendedCertifications(certs);

                return response;
            } catch (Exception e) {
                logger.error("Failed to parse Gemini output, falling back to simulated parser: " + e.getMessage());
            }
        }

        // Mock Fallback
        JdAnalysisResponse response = new JdAnalysisResponse();
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        // List of target skills to match
        String[] targetSkills = {"Java", "Spring Boot", "React", "Docker", "AWS", "SQL", "Git", "Kubernetes", "Angular", "Python", "Node", "TypeScript", "REST"};
        String jdLower = jdText.toLowerCase();

        for (String skill : targetSkills) {
            if (jdLower.contains(skill.toLowerCase())) {
                boolean userHasIt = resumeSkills.stream().anyMatch(us -> us.equalsIgnoreCase(skill));
                if (userHasIt) {
                    matched.add(skill);
                } else {
                    missing.add(skill);
                }
            }
        }

        if (missing.isEmpty()) {
            missing.add("Kubernetes");
            missing.add("TypeScript");
        }

        int score = 60 + (matched.size() * 5);
        if (score > 98) score = 98;

        response.setAtsScore(score);
        response.setMatchPercentage(score - 5);
        response.setMissingSkills(missing);
        response.setExtractedExperience("3-5 Years");
        response.setExtractedEducation("Bachelor's Degree in CS");
        response.setExtractedSalary("$120,000 - $150,000");

        List<String> suggestions = new ArrayList<>();
        suggestions.add("Add quantified metrics (e.g., 'reduced load times by 20%') to experience bullet points.");
        suggestions.add("Include missing keywords: " + String.join(", ", missing) + " directly in your resume summary.");
        suggestions.add("Ensure your resume layout is single-column to avoid parsing issues with older ATS systems.");
        response.setSuggestions(suggestions);

        List<String> certs = new ArrayList<>();
        if (missing.contains("AWS")) certs.add("AWS Certified Solutions Architect");
        if (missing.contains("Java")) certs.add("Oracle Certified Professional: Java SE Developer");
        certs.add("Scrum Alliance Certified ScrumMaster (CSM)");
        response.setRecommendedCertifications(certs);

        return response;
    }

    @Override
    public String generateInterviewQuestions(String resumeSummary, List<String> skills, 
                                              String jobTitle, String company, String jdText) {
        String prompt = String.format("Generate 4 interview questions (1 Technical, 1 HR, 1 Behavioral, 1 Coding) " +
                "with helpful answer guides, tailored for a %s role at %s, based on skills: %s.",
                jobTitle, company, String.join(", ", skills));

        String result = queryGemini(prompt);
        if (result != null) return result;

        // Mock Fallback
        return "[\n" +
                "  {\n" +
                "    \"type\": \"Technical\",\n" +
                "    \"question\": \"Explain the difference between optimistic and pessimistic locking in Spring Boot/JPA.\",\n" +
                "    \"hint\": \"Talk about @Version annotation in optimistic locking which checks record version, versus DB-level row locking in pessimistic locking.\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"Behavioral\",\n" +
                "    \"question\": \"Tell me about a time you had to optimize a slow database query. What steps did you take?\",\n" +
                "    \"hint\": \"Use the STAR method. Explain how you used EXPLAIN plans, added appropriate indexes, and optimized Hibernate fetch joins.\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"Coding\",\n" +
                "    \"question\": \"Given an integer array, return the index of two numbers such that they add up to a specific target (Two Sum).\",\n" +
                "    \"hint\": \"Mention that a HashMap can be used to solve this in O(N) time complexity by storing the complement values.\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"HR\",\n" +
                "    \"question\": \"Why do you want to join our engineering team?\",\n" +
                "    \"hint\": \"Connect your personal interests in building highly responsive SaaS architectures with the company's growth vectors.\"\n" +
                "  }\n" +
                "]";
    }

    @Override
    public List<JobRecommendation> generateJobRecommendations(User user, List<String> userSkills, String location, String title) {
        // Recommendations generated locally
        List<JobRecommendation> recommendations = new ArrayList<>();
        
        String recTitle1 = (title != null && !title.isEmpty()) ? title : "Senior Java Engineer";
        String recTitle2 = "Full Stack Engineer (Java/React)";
        
        recommendations.add(new JobRecommendation(
                user,
                recTitle1,
                "TechCorp Enterprise",
                "Java, Spring Boot, MySQL",
                92,
                (location != null && !location.isEmpty()) ? location : "San Francisco, CA",
                "$140,000 - $170,000",
                "We are seeking an experienced Developer to lead our transaction processing systems...",
                "https://techcorp.jobs/java-developer"
        ));

        recommendations.add(new JobRecommendation(
                user,
                recTitle2,
                "Innovative SaaS Labs",
                "Java, React.js, Docker",
                88,
                "Remote (US)",
                "$130,000 - $160,000",
                "Join our frontend/backend product engineering squad to scale our cloud application services...",
                "https://saaslabs.jobs/fullstack"
        ));
        
        return recommendations;
    }
}
