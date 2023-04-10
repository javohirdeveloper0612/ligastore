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
public class ProfileEntity extends AbsEntity  {



    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(nullable = false)
    private String surnameUz;

    @Column(nullable = false)
    private String surnameRu;
    @Column
    private String username;
    @Column
    private String password;

    @Column(nullable = false)
    private Date birthdate;

    @Column(nullable = false)
    private String professionUz;

    @Column(nullable = false)
    private String professionRu;

    @Column(nullable = false)
    private Boolean team;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false, unique = true)
    private String phoneUser;

    @Column(unique = true)
    private String phoneHome;
    @Column
    private Integer score;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Column
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

}