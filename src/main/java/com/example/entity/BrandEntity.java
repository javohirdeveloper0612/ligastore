package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "brand")
@Getter
@Setter
@Table
public class BrandEntity extends AbsEntity {

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(name = "attach_id", nullable = false)
    private String attachId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.REMOVE)
    private Set<CategoryEntity> category;
}
