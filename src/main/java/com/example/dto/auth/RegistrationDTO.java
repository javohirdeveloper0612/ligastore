package com.example.dto.auth;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jvnet.hk2.annotations.Optional;

import java.util.Date;

@Getter
@Setter

public class RegistrationDTO {
    @NotBlank(message = "Name required")
    private String name;

    @NotBlank(message = "Surname required")
    private String surname;
    @NotBlank(message = "Username required")
    private String username;

    @NotBlank(message = "Password required")
    private String password;

    @NotNull(message = "Birthdate required")
    private Date birthdate;

    @NotBlank(message = "Profession required")
    private String profession;

    @NotNull(message = "Team required")
    private Boolean team;

    @NotBlank(message = "Region required")
    private String region;

    @NotBlank(message = "District required")
    private String district;


    @Size(min = 13,max = 13)
    private String phoneHome;

}
