package com.example.testswimmy.repository;

import com.example.testswimmy.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Candidate> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

