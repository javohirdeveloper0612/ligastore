package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@MappedSuperclass
public abstract class AbsEntity {
    @Id
    @GenericGenerator(name = "attach_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

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
