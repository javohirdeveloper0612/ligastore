package com.example.dto.auth;

import com.example.enums.SmsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter

public class SendSmsDTO {
    @Size(min = 13, max = 13)
    @NotBlank(message = "Phone cannot be null or empty")
    private String phone;
}
