package com.example.service;

import com.example.dto.ResponseHistoryDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.entity.AdminMessageEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.AdminMessageRepository;
import com.example.repository.AuthRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AdminMessageService {
    private final AdminMessageRepository adminMessageRepository;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;


    public AdminMessageService(AdminMessageRepository adminMessageRepository,
                               AuthRepository authRepository,
                               ProductRepository productRepository) {
        this.adminMessageRepository = adminMessageRepository;
        this.authRepository = authRepository;
        this.productRepository = productRepository;

    }


    public List<AdminMessageEntity> getAllMessage() {
        return adminMessageRepository.findAllByOrderByIdDesc();
    }


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
            adminMessageRepository.save(getAdminMessageEntity(profile, product));
            return new ResponseMessage("Ball muvaffaqqiyatli yechildi", true, 200);
        }
        return new ResponseMessage("Ball yetarli emas", false, 400);
    }

    public List<ResponseHistoryDto> getUserHistory(Long user_id) {
        var list = adminMessageRepository.findAllByUserIdAndAccepted(user_id, true);
        List<ResponseHistoryDto> responseHistoryDtoList = new LinkedList<>();
        for (AdminMessageEntity message : list) responseHistoryDtoList.add(getHistoryDto(message));
        return responseHistoryDtoList;
    }

    public AdminMessageEntity getAdminMessageEntity(ProfileEntity profile, ProductEntity product) {
        AdminMessageEntity message = new AdminMessageEntity();
        message.setUserId(profile.getId());
        message.setUser_name(profile.getNameUz());
        message.setUser_surname(profile.getSurnameUz());
        message.setProduct_name(product.getNameUz());
        message.setProduct_model(product.getModel());
        message.setPhone(profile.getPhoneUser());
        message.setAccepted(true);
        message.setSellScore(product.getScore());
        return message;
    }

    public ResponseHistoryDto getHistoryDto(AdminMessageEntity message) {
        ResponseHistoryDto dto = new ResponseHistoryDto();
        dto.setId(message.getId());
        dto.setFirst_name(message.getUser_name());
        dto.setLast_name(message.getUser_surname());
        dto.setPhone(message.getPhone());
        dto.setSell_score(message.getSellScore());
        dto.setIsAccepted(message.getAccepted());
        return dto;
    }
}
