package com.example.repository;

import com.example.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByCategoryId(Long category_id);
    Page<ProductEntity> findAllByCategoryId(Long category_id, Pageable pageable);
    Optional<ProductEntity> findByModel(String model);
}
