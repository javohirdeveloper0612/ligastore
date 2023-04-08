package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;



@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity {


    @Id
    @GenericGenerator(name = "attach_uuid",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "origin_name")
    private String originName;

    @Column
    private Long size;

    @Column
    private String type;

    @Column
    private String path;

    @Column
    private Double duration;
    @OneToOne(mappedBy = "attach")
    private CategoryEntity category;

}
