package com.example.dto.auth;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginResponseDTO {
    private ProfileStatus status;
    private ProfileRole role;
    private String token;
}
