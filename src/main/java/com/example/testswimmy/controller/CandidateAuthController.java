package com.example.testswimmy.controller;

import com.example.testswimmy.model.dto.LoginRequest;
import com.example.testswimmy.model.dto.LoginResponse;
import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.model.request.CreateCandidateReq;
import com.example.testswimmy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/candidate")
@RequiredArgsConstructor
public class CandidateAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateCandidateReq candidate) {
        try {
            Candidate savedCandidate = authService.register(candidate);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Registration successful",
                            "candidateId", savedCandidate.getId(),
                            "email", savedCandidate.getEmail()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
