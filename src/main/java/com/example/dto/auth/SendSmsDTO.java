package com.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SendSmsDTO {
    @Size(min = 12, max = 12)
    @NotBlank(message = "phone cannot be null or empty")
    private String phone;
}
