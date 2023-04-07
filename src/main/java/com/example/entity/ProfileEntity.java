package com.example.entity;

import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter

@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private Date birthdate;
    @Column(nullable = false)
    private String profession;
    @Column(nullable = false)
    private Boolean team;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false,unique = true)
    private String phoneUser;
    @Column(unique = true)
    private String phoneHome;
    @Column
    private Integer ball;
    @Column
    private ProfileStatus status;

}