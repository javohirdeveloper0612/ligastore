package com.example.service;

import com.example.dto.jwt.ResponseMessage;
import com.example.entity.AdminMessageEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.exception.product.ProductNotFoundException;
import com.example.repository.AdminMesageRepository;
import com.example.repository.AuthRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminMessageService {
    private final AdminMesageRepository adminMesageRepository;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;


    public AdminMessageService(AdminMesageRepository adminMesageRepository, AuthRepository authRepository,
                               ProductRepository productRepository) {
        this.adminMesageRepository = adminMesageRepository;
        this.authRepository = authRepository;
        this.productRepository = productRepository;

    }

    public List<AdminMessageEntity> getAllMessage() {
        return adminMesageRepository.findAllByOrderByIdDesc();
    }

    public ResponseMessage checkAcceptable(Long user_id, String product_model) {
        Optional<ProductEntity> optionalProduct = productRepository.findByModel(product_model);
        if (optionalProduct.isEmpty()) {
            return new ResponseMessage(product_model + " product modeli topilmadi", false, 400);
        }
        Optional<ProfileEntity> optionalProfile = authRepository.findById(user_id);
        if (optionalProfile.isEmpty()) {
            return new ResponseMessage(user_id + " id li user topilmadi", false, 400);
        }

        ProductEntity product = optionalProduct.get();
        ProfileEntity profile = optionalProfile.get();
        Long score = profile.getScore() - product.getScore();
        profile.setScore(score);
        authRepository.save(profile);
        return new ResponseMessage("Ball muvaffaqqiyatli yechildi", true, 200);


    }
}
