package com.example.controller;

import com.example.dto.ResponsePromCode;
import com.example.enums.Language;
import com.example.service.PromoCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/promo_code")
@Tag(name = "Promo-code")
public class Promo_codeController {

    private final PromoCodeService promoCodeService;

    public Promo_codeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    public ResponseEntity<?> generatePromoCode(
            @RequestParam(name = "money") double money, @RequestParam(name = "amount") int amount,
            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {


        log.info("");
        ResponsePromCode promCode = promoCodeService.generateCode(money, amount,language);
        return ResponseEntity.status(201).body(promCode);
    }

}
