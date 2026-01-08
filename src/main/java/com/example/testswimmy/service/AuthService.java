package com.example.testswimmy.service;

import com.example.testswimmy.config.security.JwtUtil;
import com.example.testswimmy.model.dto.LoginRequest;
import com.example.testswimmy.model.dto.LoginResponse;
import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.model.mapper.UserMapper;
import com.example.testswimmy.model.request.CreateCandidateReq;
import com.example.testswimmy.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        // Xác thực với Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Lấy thông tin candidate từ database
        Candidate candidate = candidateRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Tạo JWT token
        String token = jwtUtil.generateToken(candidate.getEmail());

        return new LoginResponse(
                "Login successful",
                candidate.getId(),
                candidate.getEmail(),
                token
        );
    }

    public Candidate register(CreateCandidateReq candidate) {
        if (candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Map và mã hóa password
        Candidate user = UserMapper.toCandidate(candidate, passwordEncoder);
        return candidateRepository.save(user);
    }
}

