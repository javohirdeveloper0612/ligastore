package com.example.service;

import com.example.dto.promocode.CheckPromoCodeDTO;
import com.example.dto.promocode.CreatePromoCodeDto;
import com.example.dto.promocode.ResponsePromoCodeDto;
import com.example.dto.promocode.ResponsePromCodeMessage;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.PromoCode;
import com.example.entity.PromoCode.PromoCodeStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    /**
     * This method is used for generating PromoCode
     *
     * @param dto     CreatePromoCodeDto
     * @param product ProductEntity
     * @return List<PromoCode></>
     */

    public List<PromoCode> generatePromoCode(CreatePromoCodeDto dto, ProductEntity product) {
        Random random = new Random();
        List<PromoCode> list = new ArrayList<>();
        for (int i = 0; i < dto.getAmount(); i++) {
            String code = product.getModel() + random.nextInt(1111111, 9999999);
            long score = (long) (product.getPrice() * 0.0004);
            list.add(new PromoCode(code, score, null, product, PromoCodeStatus.ACTIVE));
        }
        return list;
    }

    /**
     * This method is used for viewing all the promo-code data by page if page or size less than 0
     * throw IllegalArgumentException If list is empty throw EmptyListException
     *
     * @param page     int
     * @param size     int
     * @param language Language
     * @return Page<PromoCodeDto></>
     */
    public List<ResponsePromoCodeDto> getListPromoCodeByPage(int page, int size, Language language) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> codes = promocodeRepository.findAll(pageable);
        if (codes.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        if (size <= 0) throw new IllegalArgumentException();
        return convertToList(codes);

    }


    /**
     * This method is used for viewing all the promo-code list data  if not found throw EmptyListException
     *
     * @param language Language
     * @return List<PromoCodeDto></>
     */
    public List<ResponsePromoCodeDto> getAllList(Language language) {
        List<PromoCode> codeList = promocodeRepository.findAll();
        if (codeList.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        return convertToList(codeList);
    }

    /**
     * This method is used for converting Page to List
     *
     * @param codes Page<PromoCode></>
     * @return List<PromoCodeDto></>
     */

    public List<ResponsePromoCodeDto> convertToList(Page<PromoCode> codes) {
        List<ResponsePromoCodeDto> list = new ArrayList<>();
        for (PromoCode code : codes) {
            list.add(responsePromoCodeDto(code));
        }
        return list;
    }

    /**
     * This method is used for converting  to List
     *
     * @param codes List<PromoCode></>
     * @return List<PromoCodeDto></>
     */
    public List<ResponsePromoCodeDto> convertToList(List<PromoCode> codes) {
        List<ResponsePromoCodeDto> list = new ArrayList<>();
        for (PromoCode code : codes) {
            list.add(responsePromoCodeDto(code));
        }
        return list;
    }


    /**
     * This method is used for converting PromoCode to ResponsePromoCodeDto
     *
     * @param code PromoCode
     * @return ResponsePromoCodeDto
     */
    public ResponsePromoCodeDto responsePromoCodeDto(PromoCode code) {
        ResponsePromoCodeDto dto = new ResponsePromoCodeDto();
        dto.setId(code.getId());
        dto.setPromo_code(code.getCode());
        dto.setScore(code.getScore());
        dto.setProduct_model(code.getProduct().getModel());
        return dto;
    }

    /**
     * This method is used for checking promo_code if the promo_code and user_id is exist
     * throw new InvalidPromoCodeException If The promo_code is not exist throw new
     * InvalidPromoCodeException
     *
     * @param promoCode long
     * @param language  Language
     * @return CheckPromoCodeDto
     */
    public CheckPromoCodeDTO check_promo_code(String promoCode, Language language) {

        ProfileEntity user = getUser(language);
        boolean exists = promocodeRepository.existsByCodeAndProfileId(promoCode, user.getId());
        if (exists)
            throw new InvalidPromoCodeException(resourceBundleService.getMessage("invalid.promo_code", language));
        Optional<PromoCode> optional = promocodeRepository.findByCode(promoCode);
        if (optional.isEmpty())
            throw new InvalidPromoCodeException(resourceBundleService.getMessage("invalid.promo_code", language));

        PromoCode code = optional.get();
        long score = user.getScore() + code.getScore();
        user.setScore(score);
        code.setProfile(user);
        code.setStatus(PromoCodeStatus.BLOCK);
        promocodeRepository.save(code);
        return new CheckPromoCodeDTO("PromoCode verification done successfully and added score", true, 200);
    }

    /**
     * This method is used for generating promo-code if amount is equals 0 or less than 0
     * throw new NotMatchException if model not found throw ProductNotFoundException
     *
     * @param dto      CreatePromoCodeDto
     * @param language Language
     * @return ResponsePromoCode
     */
    public ResponsePromCodeMessage generateCode(CreatePromoCodeDto dto, Language language) {
        if (dto.getAmount() == 0 || dto.getAmount() < 0)
            throw new NotMatchException(resourceBundleService.getMessage("not.match", language));

        Optional<ProductEntity> product = productRepository.findByModel(dto.getModel());
        if (product.isEmpty())
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        promocodeRepository.saveAll(generatePromoCode(dto, product.get()));
        return new ResponsePromCodeMessage("Successfully generated", 201, true);
    }

    /**
     * This method is used for getting promo-codeList by ProductModel if list is empty throw new EmptyListException
     *
     * @param model    String
     * @param language Language
     * @return List<ResponsePromoCode></>
     */
    public List<ResponsePromoCodeDto> findPromoCodeListByProductModel(String model, Language language) {
        List<PromoCode> codeList = promocodeRepository.findAllByProductModel(model);
        if (codeList.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        return convertToList(codeList);
    }

    /**
     * This method is used for getting user current user in System
     *
     * @param language Language
     * @return ProfileEntity
     */
    public ProfileEntity getUser(Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Optional<ProfileEntity> optional = profileRepository.findById(customUserDetail.getId());
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }
        return optional.get();
    }
}
