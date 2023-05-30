package com.example.service;

import com.example.dto.promocode.CheckPromoCodeDTO;
import com.example.dto.promocode.CreatePromoCodeDto;
import com.example.dto.promocode.ResponsePromCodeMessage;
import com.example.dto.promocode.ResponsePromoCodeDto;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.PromoCode;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.exception.product.NotMatchException;
import com.example.exception.product.ProductNotFoundException;
import com.example.exception.promocode.InvalidPromoCodeException;
import com.example.repository.ProductRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.PromoCodeRepository;
import com.example.security.CustomUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.entity.PromoCode.PromoCodeStatus.ACTIVE;
import static com.example.entity.PromoCode.PromoCodeStatus.BLOCK;

@Service
public class PromoCodeService {
    private final ResourceBundleService resourceBundleService;
    private final PromoCodeRepository promocodeRepository;
    private final ProductRepository productRepository;
    private final ProfileRepository profileRepository;

    public PromoCodeService(ResourceBundleService resourceBundleService,
                            PromoCodeRepository promocodeRepository,
                            ProductRepository productRepository, ProfileRepository profileRepository) {
        this.resourceBundleService = resourceBundleService;
        this.promocodeRepository = promocodeRepository;
        this.productRepository = productRepository;
        this.profileRepository = profileRepository;
    }


    public List<PromoCode> generatePromoCode(CreatePromoCodeDto dto, ProductEntity product) {
        var random = new Random();
        var list = new ArrayList<PromoCode>();
        for (int i = 0; i < dto.getAmount(); i++) {
            String code = product.getModel() + random.nextInt(1111111, 9999999);
            double score = (product.getPrice());
            list.add(new PromoCode(code, score, null, product, ACTIVE));
        }
        return list;
    }


    public List<ResponsePromoCodeDto> getAllList(Language language) {
        var codeList = promocodeRepository.findAll();
        if (codeList.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        return convertToList(codeList);
    }


    public List<ResponsePromoCodeDto> convertToList(List<PromoCode> codes) {
        var list = new ArrayList<ResponsePromoCodeDto>();
        for (PromoCode code : codes) list.add(responsePromoCodeDto(code));
        return list;
    }


    public ResponsePromoCodeDto responsePromoCodeDto(PromoCode code) {
        return new ResponsePromoCodeDto(code.getId(), code.getCode(), code.getScore(), code.getProduct().getModel());
    }


    public CheckPromoCodeDTO check_promo_code(String promoCode, Language language) {
        ProfileEntity user = getUser(language);
        boolean exists = promocodeRepository.existsByCodeAndProfileId(promoCode, user.getId());
        if (exists) throw new InvalidPromoCodeException(resourceBundleService.getMessage("invalid.promo_code", language));
        var optional = promocodeRepository.findByCode(promoCode);
        if (optional.isEmpty()) throw new InvalidPromoCodeException(resourceBundleService.getMessage("invalid.promo_code", language));
        PromoCode code = optional.get();
        double score = user.getScore() + code.getScore();
        user.setScore(score);
        code.setProfile(user);
        code.setStatus(BLOCK);
        promocodeRepository.save(code);
        return new CheckPromoCodeDTO("PromoCode verification done successfully and added score", true, 200);
    }


    public ResponsePromCodeMessage generateCode(CreatePromoCodeDto dto, Language language) {
        if (dto.getAmount() == 0 || dto.getAmount() < 0) throw new NotMatchException(resourceBundleService.getMessage("not.match", language));
        var product = productRepository.findByModel(dto.getModel());
        if (product.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        promocodeRepository.saveAll(generatePromoCode(dto, product.get()));
        return new ResponsePromCodeMessage("Successfully generated", 201, true);
    }


    public ProfileEntity getUser(Language language) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        var optional = profileRepository.findById(customUserDetail.getId());
        if (optional.isEmpty()) throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        return optional.get();
    }
}
