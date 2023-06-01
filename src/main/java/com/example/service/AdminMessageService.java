package com.example.service;

import com.example.dto.ResponseAdminMessage;
import com.example.dto.ResponseHistoryDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.entity.AdminMessageEntity;
import com.example.entity.ProductEntity;
import com.example.repository.AdminMessageRepository;
import com.example.repository.AuthRepository;
import com.example.repository.ProductRepository;
import com.example.security.CustomUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class AdminMessageService {
    private final AdminMessageRepository adminMessageRepository;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;


    public AdminMessageService(AdminMessageRepository adminMessageRepository, AuthRepository authRepository, ProductRepository productRepository) {
        this.adminMessageRepository = adminMessageRepository;
        this.authRepository = authRepository;
        this.productRepository = productRepository;
    }


    public List<ResponseAdminMessage> getAllMessage() {
        var list = adminMessageRepository.findAllByOrderByIdDesc();
        var adminMessageList = new LinkedList<ResponseAdminMessage>();
        for (AdminMessageEntity message : list) adminMessageList.add(getResponseAdminMessage(message));
        return adminMessageList;
    }

    public ResponseAdminMessage getResponseAdminMessage(AdminMessageEntity message) {
        var adminMessage = new ResponseAdminMessage();
        adminMessage.setId(message.getId());
        adminMessage.setFirst_name(message.getUser_name());
        adminMessage.setLast_name(message.getUser_surname());
        adminMessage.setPhone(message.getPhone());
        adminMessage.setProduct_name(message.getProduct_name());
        adminMessage.setProduct_model(message.getProduct_model());
        adminMessage.setAccepted(message.getAccepted());
        return adminMessage;
    }

    public ResponseMessage checkAcceptable(Long orderId) {
        var admin = adminMessageRepository.findById(orderId);
        if (admin.isEmpty()) return new ResponseMessage(orderId + "ushbu Buyurtma ID topilmadi", false, 400);
        var product = productRepository.findByModel(admin.get().getProduct_model());
        if (product.isEmpty())
            return new ResponseMessage(admin.get().getProduct_model() + " product modeli topilmadi", false, 400);
        var profile = authRepository.findById(admin.get().getUserId());
        if (profile.isEmpty())
            return new ResponseMessage(admin.get().getUserId() + " ushbu Id li profile mavjud emas", false, 400);
        if (profile.get().getScore() < product.get().getScore())
            return new ResponseMessage("Ball yetarli emas", false, 400);
        profile.get().setScore(profile.get().getScore() - product.get().getScore());
        authRepository.save(profile.get());
        adminMessageRepository.save(getAdminMessageEntity(product.get(), admin.get()));
        return new ResponseMessage("Ball muvaffaqqiyatli yechildi", true, 200);
    }

    public List<ResponseHistoryDto> getUserHistory() {
        var list = adminMessageRepository.findAllByUserIdAndAccepted(getUserId(), true);
        var responseHistoryDtoList = new LinkedList<ResponseHistoryDto>();
        for (AdminMessageEntity message : list) responseHistoryDtoList.add(getHistoryDto(message));
        return responseHistoryDtoList;
    }

    public AdminMessageEntity getAdminMessageEntity(ProductEntity product, AdminMessageEntity message) {
        message.setAccepted(true);
        message.setSellScore(product.getScore());
        return message;
    }

    public ResponseHistoryDto getHistoryDto(AdminMessageEntity message) {
        var dto = new ResponseHistoryDto();
        dto.setProduct_model(message.getProduct_model());
        dto.setSell_score(message.getSellScore());
        dto.setFileUrl(message.getFileUrl());
        return dto;
    }

    private Long getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (CustomUserDetail) authentication.getPrincipal();
        return user.getId();
    }

    @Transactional
    public ResponseMessage deleteMessage(Long id) {
        var optional = adminMessageRepository.findById(id);
        if (optional.isEmpty())
            return new ResponseMessage(" ushbu " + id + " raqamli  message topilmadio ", false, 400);
        adminMessageRepository.delete(optional.get());
        return new ResponseMessage("Muvaffaqqiyatli o'chirildi", true, 200);
    }
}
