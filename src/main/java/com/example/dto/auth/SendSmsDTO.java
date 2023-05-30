package com.example.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SendSmsDTO {
    @Size(min = 13, max = 13)
    @NotEmpty(message = "Phone cannot be null or empty")
    private String phone;
}
