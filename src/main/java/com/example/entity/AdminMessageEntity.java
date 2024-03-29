package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "product_user")
@Getter
@Setter
public class AdminMessageEntity extends AbsEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false)
    private String user_surname;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private String product_model;


    private String fileUrl;

    @Column(nullable = false)
    private Boolean accepted;

    @Column
    private Double sellScore;

}
