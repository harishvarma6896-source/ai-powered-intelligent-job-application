USE job_assistant;

-- 1. Insert Users (Password is 'password' BCrypt hashed)
-- Admin
INSERT INTO users (id, username, email, password, full_name, role, status)
VALUES (1, 'admin', 'admin@jobassistant.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20sg5FCeqPmgW7Y971Ur1dBm3tp9.ae', 'System Administrator', 'ROLE_ADMIN', 'ACTIVE');

-- User
INSERT INTO users (id, username, email, password, full_name, role, status)
VALUES (2, 'john_doe', 'john.doe@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20sg5FCeqPmgW7Y971Ur1dBm3tp9.ae', 'John Doe', 'ROLE_USER', 'ACTIVE');

-- 2. Insert User Profiles
INSERT INTO user_profiles (id, user_id, title, bio, phone, location, portfolio_url, linkedin_url, github_url)
VALUES (1, 2, 'Senior Full Stack Java Engineer', 'Passionate developer with 5+ years of experience building high-scale SaaS products with Java, Spring Boot, and React.', '+1234567890', 'San Francisco, CA', 'https://johndoe.dev', 'https://linkedin.com/in/johndoe', 'https://github.com/johndoe');

-- 3. Insert Skills
INSERT INTO skills (profile_id, name, proficiency) VALUES 
(1, 'Java', 'Expert'),
(1, 'Spring Boot', 'Expert'),
(1, 'React.js', 'Advanced'),
(1, 'MySQL', 'Advanced'),
(1, 'Docker', 'Intermediate'),
(1, 'AWS', 'Intermediate'),
(1, 'REST APIs', 'Expert'),
(1, 'Git', 'Expert');

-- 4. Insert Education
INSERT INTO education (profile_id, school, degree, field_of_study, start_date, end_date, description) VALUES
(1, 'Stanford University', 'Master of Science', 'Computer Science', '2017-09-01', '2019-06-01', 'Specialized in Software Engineering and Distributed Systems.');

-- 5. Insert Experience
INSERT INTO experience (profile_id, company, position, location, start_date, end_date, description, currently_working) VALUES
(1, 'TechCorp Solutions', 'Software Engineer II', 'San Francisco, CA', '2019-07-01', '2022-12-31', 'Designed and optimized high-performance Spring Boot REST APIs and migrated frontend to React.', FALSE),
(1, 'CloudScale Inc', 'Senior Software Engineer', 'Remote', '2023-01-01', NULL, 'Leading a team of 4 to design microservices architecture and deploy applications using AWS and Kubernetes.', TRUE);

-- 6. Insert Projects
INSERT INTO projects (profile_id, title, description, technologies, link) VALUES
(1, 'E-Commerce Microservices Platform', 'A highly scalable e-commerce application processing 10k+ orders per day built on Spring Cloud and React.', 'Spring Boot, Spring Cloud, React, MySQL, Docker, Kafka', 'https://github.com/johndoe/ecommerce-microservices');

-- 7. Insert Certifications
INSERT INTO certifications (profile_id, name, issuing_organization, issue_date, expiration_date, credential_id) VALUES
(1, 'AWS Certified Solutions Architect', 'Amazon Web Services', '2024-01-15', '2027-01-15', 'AWS-ASA-9988');

-- 8. Insert Applications
INSERT INTO applications (id, user_id, company_name, job_title, salary, location, applied_date, deadline, status, notes) VALUES
(1, 2, 'Google', 'Senior Java Developer', '$180,000', 'Mountain View, CA', '2026-07-01', '2026-08-01', 'Interview', 'First round HR screening completed. Technical interview scheduled.'),
(2, 2, 'Netflix', 'Backend Engineer', '$220,000', 'Los Gatos, CA', '2026-07-05', '2026-08-15', 'Assessment', 'Coding challenge received, deadline next week.'),
(3, 2, 'Amazon', 'Software Development Engineer II', '$165,000', 'Seattle, WA', '2026-06-15', '2026-07-15', 'Rejected', 'Passed technical rounds, rejected on systems design.'),
(4, 2, 'Stripe', 'Full Stack Developer', '$190,000', 'San Francisco, CA', '2026-07-10', '2026-08-10', 'Applied', 'Applied through referral. Waiting for response.');

-- 9. Insert Notifications
INSERT INTO notifications (user_id, message, is_read, type) VALUES
(2, 'Your ATS Score for Google - Senior Java Developer application is 85%!', FALSE, 'ATS_SCORE'),
(2, 'Reminder: Netflix coding assessment is due in 3 days.', FALSE, 'REMINDER'),
(2, 'New job recommendation matching your Java and React skills found.', TRUE, 'RECOMMENDATION');

-- 10. Insert Resumes
INSERT INTO resumes (user_id, title, content_json, ats_score, improvement_suggestions, template_name, version, is_active) VALUES
(2, 'Standard Java Full Stack Resume', '{"summary":"Senior Java Full Stack Engineer with 5+ years of experience...","skills":["Java","Spring Boot","React","SQL"],"experience":[{"company":"CloudScale","position":"Senior Engineer","years":3.5}]}', 82, '1. Add more metrics to experience section.\n2. Include Docker and Kubernetes in summary.', 'Professional', 1, TRUE);

-- 11. Insert Cover Letters
INSERT INTO cover_letters (user_id, recipient, company, job_title, tone, content) VALUES
(2, 'Hiring Team', 'Google', 'Senior Java Developer', 'Professional', 'Dear Hiring Manager,\n\nI am writing to express my interest in the Senior Java Developer role at Google. With over 5 years of industry experience developing scalable backend systems using Java and Spring Boot...');

-- 12. Insert Activity Logs
INSERT INTO activity_logs (user_id, action, description, ip_address) VALUES
(2, 'LOGIN', 'User logged in successfully.', '127.0.0.1'),
(2, 'RESUME_CREATE', 'Created resume: Standard Java Full Stack Resume', '127.0.0.1');

-- 13. Insert Feedback
INSERT INTO feedback (user_id, email, rating, comment) VALUES
(2, 'john.doe@example.com', 5, 'This is the best tool I have used for managing my job search. The ATS analysis is spot-on!');
