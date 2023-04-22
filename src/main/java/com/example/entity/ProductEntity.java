package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "product")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity extends AbsEntity {

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long score;

    @Column(nullable = false)
    private String descriptionUz;

    @Column(nullable = false)
    private String descriptionRu;

    @Column(nullable = false, unique = true)
    private String model;

    @ManyToOne(optional = false)
    private CategoryEntity category;


    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;


}
