package com.example.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginDTO {

    private String username;

    @Size(min = 8, message = "Password required")
    private String password;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
