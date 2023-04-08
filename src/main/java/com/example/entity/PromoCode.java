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

    @Id
    @Column
    private Long id;

    @Column
    private Long code;

    @Column(nullable = false)
    private Integer score;
    @ManyToOne
    private ProfileEntity profile;
    @Column
    private Double money;


}
