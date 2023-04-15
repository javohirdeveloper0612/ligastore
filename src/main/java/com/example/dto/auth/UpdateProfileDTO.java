package com.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class UpdateProfileDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "surname required")

    private String surname;
    @NotBlank(message = "username required")

    private String profession;
    @NotNull(message = "team required")

    @NotBlank(message = "region required")
    private String region;
    @NotBlank(message = "district required")

    private String district;
    private String phoneHome;
}
