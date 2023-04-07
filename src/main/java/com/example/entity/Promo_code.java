package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promo_code", uniqueConstraints = @UniqueConstraint(columnNames = {"", ""}))
public class Promo_code extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String promo_code;

    @Column(nullable = false)
    private Integer score;

    @ManyToOne(optional = false)
    private ProfileEntity profile;
}
