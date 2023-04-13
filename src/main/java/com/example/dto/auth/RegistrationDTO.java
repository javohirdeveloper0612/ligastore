package com.example.dto.auth;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class RegistrationDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "surname required")

    private String surname;
    @NotBlank(message = "username required")

    private String username;
    @NotBlank(message = "password required")

    private String password;
    @NotNull(message = "birthdate required")

    private Date birthdate;
    @NotBlank(message = "profession required")

    private String profession;
    @NotNull(message = "team required")

    private Boolean team;

    @NotBlank(message = "region required")

    private String region;
    @NotBlank(message = "district required")

    private String district;
    private String phoneHome;

}
