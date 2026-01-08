package com.example.testswimmy.model.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCandidateReq {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, max = 100, message = " Phone must be between 6 and 100 characters")
    private String phone;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
}
