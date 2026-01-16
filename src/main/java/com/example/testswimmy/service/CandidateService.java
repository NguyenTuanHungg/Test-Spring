package com.example.testswimmy.service;

import com.example.testswimmy.entity.Candidate;
import com.example.testswimmy.model.dto.CandidateDto;
import com.example.testswimmy.model.dto.PageResponse;
import com.example.testswimmy.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public PageResponse<CandidateDto> getCandidates(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Candidate> candidatePage;

        if (name != null && !name.trim().isEmpty()) {
            candidatePage = candidateRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        } else {
            candidatePage = candidateRepository.findAll(pageable);
        }

        List<CandidateDto> candidateDtos = candidatePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                candidateDtos,
                candidatePage.getNumber(),
                candidatePage.getSize(),
                candidatePage.getTotalElements(),
                candidatePage.getTotalPages(),
                candidatePage.isLast(),
                candidatePage.isFirst()
        );
    }

    private CandidateDto convertToDto(Candidate candidate) {
        CandidateDto dto = new CandidateDto();
        dto.setId(candidate.getId());
        dto.setEmail(candidate.getEmail());
        dto.setPhone(candidate.getPhone());
        dto.setName(candidate.getName());
        dto.setCreatedAt(candidate.getCreatedAt());
        dto.setUpdatedAt(candidate.getUpdatedAt());
        return dto;
    }
}


