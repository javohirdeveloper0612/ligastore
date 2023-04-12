package com.example.repository;

import com.example.entity.ProductEntity;
import com.example.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocodeRepository extends JpaRepository<PromoCode, Long> {
    boolean existsByCodeAndProfileId(Long code, Long profile_id);

    Optional<PromoCode> findByCode(Long code);

    List<PromoCode> findAllByProductModel(String product_model);
}
