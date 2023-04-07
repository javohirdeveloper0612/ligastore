package com.example.repostoriy;
import com.example.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepostoriy extends JpaRepository<CategoryEntity,Long> {


}
