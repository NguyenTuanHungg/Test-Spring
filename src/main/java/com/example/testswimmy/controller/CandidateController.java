package com.example.testswimmy.controller;

import com.example.testswimmy.model.dto.CandidateDto;
import com.example.testswimmy.model.dto.PageResponse;
import com.example.testswimmy.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<PageResponse<CandidateDto>> getCandidates(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<CandidateDto> response = candidateService.getCandidates(name, page, size);
        return ResponseEntity.ok(response);
    }
}

