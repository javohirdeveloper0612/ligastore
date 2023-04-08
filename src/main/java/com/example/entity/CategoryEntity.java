package com.example.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter

@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity extends AbsEntity {

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(name = "attach_id")
    private String attachId;

    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @ManyToOne
    private CategoryEntity parentCategory;

}
