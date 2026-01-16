package com.example.testswimmy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDto {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

