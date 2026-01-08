package com.example.testswimmy.service;

import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateUserDetailsService implements UserDetailsService {

    private final CandidateRepository candidateRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Candidate not found with email: " + email));

        return User.builder()
                .username(candidate.getEmail())
                .password(candidate.getPassword())
                .roles("CANDIDATE")
                .build();
    }
}

