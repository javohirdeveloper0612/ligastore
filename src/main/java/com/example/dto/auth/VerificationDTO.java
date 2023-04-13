package com.example.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDTO {
    @Size(min = 12, max = 12)
    @NotBlank(message = "phone cannot be null or empty")
    private String phone;
    @NotBlank(message = "password required")
    private String password;
}
