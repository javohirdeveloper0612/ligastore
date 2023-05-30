package com.example.repository;

import com.example.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    boolean existsByCodeAndProfileId(String code, Long profile_id);

    Optional<PromoCode> findByCode(String code);

   List<PromoCode> findAllByProductModelAndStatus(String product_model, PromoCode.PromoCodeStatus status);



    List<PromoCode> findAllByProductModel(String model);
}
