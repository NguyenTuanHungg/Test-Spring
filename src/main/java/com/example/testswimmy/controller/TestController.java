package com.example.testswimmy.controller;

import com.example.testswimmy.job.CandidateReminderJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CandidateReminderJob candidateReminderJob;

    /**
     * Endpoint to manually trigger the daily work reminder job
     * Useful for testing without waiting for the scheduled time
     *
     * POST http://localhost:8080/api/test/send-reminders
     */
    @PostMapping("/send-reminders")
    public ResponseEntity<Map<String, String>> triggerEmailReminders() {
        try {
            candidateReminderJob.sendDailyWorkReminder();

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Email reminders sent successfully. Check logs for details.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send email reminders: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}

