package com.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VerificationDTO {
    @Size(min = 13, max = 13)
    @NotBlank(message = "phone cannot be null or empty")
    private String phone;
}
