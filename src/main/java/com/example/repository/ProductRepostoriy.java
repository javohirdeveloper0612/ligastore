package com.example.repository;
import com.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepostoriy extends JpaRepository<ProductEntity,Integer> {



}
