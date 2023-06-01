package com.example.entity;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity(name = "profile")
public class ProfileEntity extends AbsEntity {

    @Column
    private String nameUz;

    @Column
    private String nameRu;

    @Column
    private String surnameUz;

    @Column
    private String surnameRu;
    @Column
    private Date birthdate;

    @Column
    private String professionUz;

    @Column
    private String professionRu;

    @Column
    private Boolean team;

    @Column
    private String region;

    @Column
    private String district;

    @Column(nullable = false, unique = true)
    private String phoneUser;


    @Column(unique = true)
    private String phoneHome;


    @Column(nullable = false)
    private String smsCode;
    @Column
    private LocalDateTime smsTime;

    @Column
    private double score;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Column

    @Enumerated(EnumType.STRING)
    private ProfileRole role;

}