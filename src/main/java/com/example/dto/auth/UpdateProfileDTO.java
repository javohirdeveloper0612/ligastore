package com.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateProfileDTO {
    @NotBlank(message = "name required")
    private String name;

    @NotBlank(message = "surname required")
    private String surname;

    @NotBlank(message = "Profession required")
    private String profession;

    @NotBlank(message = "Region required")
    private String region;

    @NotBlank(message = "District required")
    private String district;

    @Size(min = 13,max = 13)
    private String phoneHome;
}
