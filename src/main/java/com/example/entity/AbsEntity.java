package com.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@MappedSuperclass
@Getter
@Setter
public abstract class AbsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private Timestamp createdDate;


    @Column
    @UpdateTimestamp
    private Timestamp updatedDate;


    @ManyToOne
    @CreatedBy
    private ProfileEntity createdBy;

    @ManyToOne
    @LastModifiedBy
    private ProfileEntity updatedBy;
}
