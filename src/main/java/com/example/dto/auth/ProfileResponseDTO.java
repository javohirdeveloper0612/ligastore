package com.example.dto.auth;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String surnameUz;
    private String surnameRu;
    private Date birthdate;
    private String professionUz;
    private String professionRu;
    private Boolean team;
    private String regionUz;
    private String regionRu;
    private String districtUz;
    private String districtRu;
    private String phoneUser;
    private String message;
    private String phoneHome;
    private Double score;
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    private ProfileRole role;
}
