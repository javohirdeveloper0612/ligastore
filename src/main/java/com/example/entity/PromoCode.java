package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "profile_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode extends AbsEntity {

    @Column
    private String code;


    @Column(nullable = false)
    private Double score;


    @ManyToOne(cascade = CascadeType.ALL)
    private ProfileEntity profile;


    @ManyToOne
    private ProductEntity product;


    @Enumerated(EnumType.STRING)
    private PromoCodeStatus status;

    public enum PromoCodeStatus {ACTIVE, BLOCK}

}
