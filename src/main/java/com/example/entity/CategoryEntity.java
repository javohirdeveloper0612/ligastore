package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "category")
public class CategoryEntity extends AbsEntity {

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(name = "attach_id")
    private String attachId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;


    @ManyToOne(optional = false)
    private BrandEntity brand;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<ProductEntity> productEntity;

}
