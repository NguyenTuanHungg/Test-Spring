package com.example.testswimmy.model.mapper;

import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.model.request.CreateCandidateReq;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserMapper {

    public static Candidate toCandidate(CreateCandidateReq req, PasswordEncoder passwordEncoder) {
        Candidate candidate = new Candidate();
        candidate.setName(req.getName());
        candidate.setEmail(req.getEmail());
        candidate.setPhone(req.getPhone());
        candidate.setPassword(passwordEncoder.encode(req.getPassword()));

        return candidate;
    }
}
