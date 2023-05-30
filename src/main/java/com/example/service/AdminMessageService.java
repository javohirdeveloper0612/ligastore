package com.example.service;

import com.example.dto.jwt.ResponseMessage;
import com.example.entity.AdminMessageEntity;
import com.example.repository.AdminMesageRepository;
import com.example.repository.AuthRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMessageService {
    private final AdminMesageRepository adminMesageRepository;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;


    public AdminMessageService(AdminMesageRepository adminMesageRepository,
                               AuthRepository authRepository,
                               ProductRepository productRepository) {
        this.adminMesageRepository = adminMesageRepository;
        this.authRepository = authRepository;
        this.productRepository = productRepository;

    }

    /**
     * This method is used for getting message list
     *
     * @return List<AdminMessageEntity></>
     */
    public List<AdminMessageEntity> getAllMessage() {
        return adminMesageRepository.findAllByOrderByIdDesc();
    }

    /**
     * This method is used for checking user press accept button or not
     * If not founded user_id throw new ProfileNotFoundException
     * If not founded product_model throw new ProductNotFoundException
     *
     * @param user_id       Long
     * @param product_model String
     * @return ResponseMessage
     */
    public ResponseMessage checkAcceptable(Long user_id, String product_model) {
        var optionalProduct = productRepository.findByModel(product_model);
        if (optionalProduct.isEmpty()) return new ResponseMessage(product_model + " product modeli topilmadi",
                false, 400);

        var optionalProfile = authRepository.findById(user_id);
        if (optionalProfile.isEmpty()) return new ResponseMessage(user_id + " id li user topilmadi",
                false, 400);

        var product = optionalProduct.get();
        var profile = optionalProfile.get();

        if (profile.getScore() >= product.getScore()) {
            Long score = profile.getScore() - product.getScore();
            profile.setScore(score);
            authRepository.save(profile);
            return new ResponseMessage("Ball muvaffaqqiyatli yechildi", true, 200);
        }
        return new ResponseMessage("Ball yetarli emas", false, 400);

    }
}
