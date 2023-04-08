package com.example.service;
import com.example.dto.PromoCodeDto;
import com.example.dto.ResponsePromCode;
import com.example.entity.PromoCode;
import com.example.enums.Language;
import com.example.exception.EmptyListException;
import com.example.exception.NotMatchException;
import com.example.repository.PromocodeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PromoCodeService {
    private final ResourceBundleService resourceBundleService;
    private final PromocodeRepository promocodeRepository;

    public PromoCodeService(ResourceBundleService resourceBundleService, PromocodeRepository promocodeRepository) {
        this.resourceBundleService = resourceBundleService;
        this.promocodeRepository = promocodeRepository;
    }

    /**
     * This method is used for generating promo-code If amount ==0 or amount<=0
     * throw NotMatchException
     *
     * @param money    double
     * @param amount   int
     * @param language Language
     * @return ResponseGenerateDto
     */

    public ResponsePromCode generateCode(double money, int amount, Language language) {
        if (amount == 0 || amount < 0) {
            throw new NotMatchException(resourceBundleService.getMessage("not.match", language));
        }
        promocodeRepository.saveAll(generatePromoCode(amount, money));
        return new ResponsePromCode("Successfully generated", 201, true);
    }


    /**
     * This method is used for generating promo-code
     * @param amount int
     * @param money double
     * @return List<PromoCode></>
     */
    public List<PromoCode> generatePromoCode(int amount, double money) {
        Random random = new Random();
        List<PromoCode> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            long code = random.nextInt(111111, 999999);
            int score = (int) (money * 0.04);
            list.add(new PromoCode(code, score, null, money));
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
    public List<PromoCodeDto> getListPromoCodeByPage(int page, int size, Language language) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> codes = promocodeRepository.findAll(pageable);
        if (codes.isEmpty()) {
            throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        }
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        return convertToList(codes);

    }


    /**
     * This method is used for viewing all the promoce list data  if not found throw EmptyListException
     *
     * @param language Language
     * @return List<PromoCodeDto></>
     */
    public List<PromoCodeDto> getAllList(Language language) {
        List<PromoCode> codeList = promocodeRepository.findAll();
        if (codeList.isEmpty()) {
            throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        }
        return convertToList(codeList);
    }


    /**
     * This method is used for converting Page to List
     *
     * @param codes Page<PromoCode></>
     * @return List<PromoCodeDto></>
     */

    public List<PromoCodeDto> convertToList(Page<PromoCode> codes) {
        List<PromoCodeDto> list = new ArrayList<>();
        for (PromoCode code : codes) {
            PromoCodeDto dto = new PromoCodeDto();
            dto.setId(code.getId());
            dto.setMoney(code.getMoney());
            dto.setPromo_code(code.getCode());
            dto.setScore(code.getScore());
            list.add(dto);
        }
        return list;
    }

    /**
     * This method is used for converting  to List
     *
     * @param codes List<PromoCode></>
     * @return List<PromoCodeDto></>
     */
    public List<PromoCodeDto> convertToList(List<PromoCode> codes) {
        List<PromoCodeDto> list = new ArrayList<>();
        for (PromoCode code : codes) {
            PromoCodeDto dto = new PromoCodeDto();
            dto.setId(code.getId());
            dto.setMoney(code.getMoney());
            dto.setPromo_code(code.getCode());
            dto.setScore(code.getScore());
            list.add(dto);
        }
        return list;
    }
}
