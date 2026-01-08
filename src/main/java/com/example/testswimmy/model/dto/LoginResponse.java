package com.example.testswimmy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private Long candidateId;
    private String email;
    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(String message, Long candidateId, String email, String token) {
        this.message = message;
        this.candidateId = candidateId;
        this.email = email;
        this.token = token;
        this.tokenType = "Bearer";
    }
}

