package com.example.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Column
    private String attachId;

    @ManyToOne(optional = false)
    private CategoryEntity category;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    private AttachEntity photo;


}
