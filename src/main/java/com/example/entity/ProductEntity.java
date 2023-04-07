package com.example.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nameUz;
    @Column
    private String nameRu;
    @Column
    private Double price;
    @Column
    private Integer ball;
    @Column
    private String descriptionUz;
    @Column
    private String descriptionRu;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;
}
