package com.jobappassistant.config;

import com.jobappassistant.entity.*;
import com.jobappassistant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

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
    private JobApplicationRepository applicationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private CoverLetterRepository coverLetterRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        // 1. Users
        User admin = new User("admin", "admin@jobassistant.com", passwordEncoder.encode("password"), "System Administrator", "ROLE_ADMIN", "ACTIVE");
        User user = new User("john_doe", "john.doe@example.com", passwordEncoder.encode("password"), "John Doe", "ROLE_USER", "ACTIVE");
        
        userRepository.save(admin);
        userRepository.save(user);

        // 2. User Profile for John Doe
        UserProfile profile = new UserProfile(user, "Senior Full Stack Java Engineer", 
                "Passionate developer with 5+ years of experience building high-scale SaaS products with Java, Spring Boot, and React.", 
                "+1234567890", "San Francisco, CA");
        profile.setPortfolioUrl("https://johndoe.dev");
        profile.setLinkedinUrl("https://linkedin.com/in/johndoe");
        profile.setGithubUrl("https://github.com/johndoe");
        profileRepository.save(profile);

        // Bootstrap profile for Admin too (UserProfileServiceImpl bootstraps profile on register)
        UserProfile adminProfile = new UserProfile(admin, "System Administrator", "Manages the platform metrics.", "", "Global");
        profileRepository.save(adminProfile);

        // 3. Skills
        skillRepository.save(new Skill(profile, "Java", "Expert"));
        skillRepository.save(new Skill(profile, "Spring Boot", "Expert"));
        skillRepository.save(new Skill(profile, "React.js", "Advanced"));
        skillRepository.save(new Skill(profile, "MySQL", "Advanced"));
        skillRepository.save(new Skill(profile, "Docker", "Intermediate"));
        skillRepository.save(new Skill(profile, "AWS", "Intermediate"));
        skillRepository.save(new Skill(profile, "REST APIs", "Expert"));
        skillRepository.save(new Skill(profile, "Git", "Expert"));

        // 4. Education
        educationRepository.save(new Education(profile, "Stanford University", "Master of Science", "Computer Science", 
                LocalDate.of(2017, 9, 1), LocalDate.of(2019, 6, 1), 
                "Specialized in Software Engineering and Distributed Systems."));

        // 5. Experience
        experienceRepository.save(new Experience(profile, "TechCorp Solutions", "Software Engineer II", "San Francisco, CA", 
                LocalDate.of(2019, 7, 1), LocalDate.of(2022, 12, 31), 
                "Designed and optimized high-performance Spring Boot REST APIs and migrated frontend to React.", false));
        experienceRepository.save(new Experience(profile, "CloudScale Inc", "Senior Software Engineer", "Remote", 
                LocalDate.of(2023, 1, 1), null, 
                "Leading a team of 4 to design microservices architecture and deploy applications using AWS and Kubernetes.", true));

        // 6. Projects
        projectRepository.save(new Project(profile, "E-Commerce Microservices Platform", 
                "A highly scalable e-commerce application processing 10k+ orders per day built on Spring Cloud and React.", 
                "Spring Boot, Spring Cloud, React, MySQL, Docker, Kafka", "https://github.com/johndoe/ecommerce-microservices"));

        // 7. Certifications
        certificationRepository.save(new Certification(profile, "AWS Certified Solutions Architect", "Amazon Web Services", 
                LocalDate.of(2024, 1, 15), LocalDate.of(2027, 1, 15), "AWS-ASA-9988", ""));

        // 8. Applications
        applicationRepository.save(new JobApplication(user, "Google", "Senior Java Developer", "$180,000", "Mountain View, CA", 
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 1), "Interview", 
                "First round HR screening completed. Technical interview scheduled."));
        applicationRepository.save(new JobApplication(user, "Netflix", "Backend Engineer", "$220,000", "Los Gatos, CA", 
                LocalDate.of(2026, 7, 5), LocalDate.of(2026, 8, 15), "Assessment", 
                "Coding challenge received, deadline next week."));
        applicationRepository.save(new JobApplication(user, "Amazon", "Software Development Engineer II", "$165,000", "Seattle, WA", 
                LocalDate.of(2026, 6, 15), LocalDate.of(2026, 7, 15), "Rejected", 
                "Passed technical rounds, rejected on systems design."));
        applicationRepository.save(new JobApplication(user, "Stripe", "Full Stack Developer", "$190,000", "San Francisco, CA", 
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 8, 10), "Applied", 
                "Applied through referral. Waiting for response."));

        // 9. Notifications
        notificationRepository.save(new Notification(user, "Your ATS Score for Google - Senior Java Developer application is 85%!", false, "ATS_SCORE"));
        notificationRepository.save(new Notification(user, "Reminder: Netflix coding assessment is due in 3 days.", false, "REMINDER"));
        notificationRepository.save(new Notification(user, "New job recommendation matching your Java and React skills found.", true, "RECOMMENDATION"));

        // 10. Resumes
        resumeRepository.save(new Resume(user, "Standard Java Full Stack Resume", 
                "{\"summary\":\"Senior Java Full Stack Engineer with 5+ years of experience...\",\"skills\":[\"Java\",\"Spring Boot\",\"React\",\"SQL\"],\"experience\":[{\"company\":\"CloudScale\",\"position\":\"Senior Engineer\",\"years\":3.5}]}", 
                82, "1. Add more metrics to experience section.\n2. Include Docker and Kubernetes in summary.", "Professional", 1));

        // 11. Cover Letters
        coverLetterRepository.save(new CoverLetter(user, "Hiring Team", "Google", "Senior Java Developer", "Professional", 
                "Dear Hiring Manager,\n\nI am writing to express my interest in the Senior Java Developer role at Google. With over 5 years of industry experience developing scalable backend systems using Java and Spring Boot..."));

        // 12. Activity Logs
        activityLogRepository.save(new ActivityLog(user, "LOGIN", "User logged in successfully.", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(user, "RESUME_CREATE", "Created resume: Standard Java Full Stack Resume", "127.0.0.1"));

        // 13. Feedback
        feedbackRepository.save(new Feedback(user, "john.doe@example.com", 5, "This is the best tool I have used for managing my job search. The ATS analysis is spot-on!"));
    }
}
