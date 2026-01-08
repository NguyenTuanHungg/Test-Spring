package com.example.testswimmy.model.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    private String name;

    private String email;

    private String phone;

}
