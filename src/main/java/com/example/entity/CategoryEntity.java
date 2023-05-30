package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "category")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity extends AbsEntity {

    public CategoryEntity(String nameUz, String nameRu, String attachId, BrandEntity brand) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.attachId = attachId;
        this.brand = brand;
    }

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
