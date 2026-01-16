package com.example.testswimmy.job;

import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.repository.CandidateRepository;
import com.example.testswimmy.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CandidateReminderJob {

    private final CandidateRepository candidateRepository;
    private final EmailService emailService;

    /**
     * Scheduled job that runs every day at 5:00 AM
     * Sends work reminder emails to all candidates
     */
    @Scheduled(cron = "0 0 5 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void sendDailyWorkReminder() {
        log.info("Starting daily work reminder job at 5:00 AM");

        try {
            // Get all candidates from database
            List<Candidate> candidates = candidateRepository.findAll();

            if (candidates.isEmpty()) {
                log.info("No candidates found to send reminders");
                return;
            }

            log.info("Found {} candidates. Sending reminder emails...", candidates.size());

            int successCount = 0;
            int failureCount = 0;

            for (Candidate candidate : candidates) {
                try {
                    // Only send email if candidate has an email address
                    if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
                        emailService.sendWorkReminderEmail(
                            candidate.getEmail(),
                            candidate.getName()
                        );
                        successCount++;
                        log.info("Sent reminder email to: {} ({})",
                            candidate.getName(), candidate.getEmail());
                    } else {
                        log.warn("Candidate {} has no email address", candidate.getId());
                        failureCount++;
                    }
                } catch (Exception e) {
                    failureCount++;
                    log.error("Failed to send email to candidate: {} ({}). Error: {}",
                        candidate.getName(), candidate.getEmail(), e.getMessage());
                }
            }

            log.info("Daily work reminder job completed. Success: {}, Failed: {}",
                successCount, failureCount);

        } catch (Exception e) {
            log.error("Error in daily work reminder job: {}", e.getMessage(), e);
        }
    }

    /**
     * Test method - runs every day at 8:00 AM for testing
     * You can remove this or change the schedule as needed
     */
    // @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void sendTestReminder() {
        log.info("Test reminder job executed at 8:00 AM");
        // This is disabled by default, uncomment the @Scheduled annotation to enable
    }
}

