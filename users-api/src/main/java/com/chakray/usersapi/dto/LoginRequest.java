package com.chakray.usersapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String taxId;

    @NotBlank
    private String password;
}
