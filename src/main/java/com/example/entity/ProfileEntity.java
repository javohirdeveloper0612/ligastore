package com.example.entity;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter

@Entity
@Table(name = "profile")
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
    private String username;
    @Column
    private String password;

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
    private Long score;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Column
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

}