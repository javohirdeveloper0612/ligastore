package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity(name = "brand")
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity extends AbsEntity {

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(name = "attach_id", nullable = false)
    private String attachId;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.REMOVE)
    private Set<CategoryEntity> category;

    public BrandEntity(String nameUz, String nameRu, String attachId) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.attachId = attachId;
    }
}
