package com.example.controller;

import com.example.dto.promocode.CreatePromoCodeDto;
import com.example.enums.Language;
import com.example.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/promo_code")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }


    @Operation(summary = "GENERATE PROMO_CODE API", description = "Ushbu API promo_code generatsiya qilish uchun ishlatiladi ")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/generate")
    public ResponseEntity<?> generatePromoCode(@RequestBody CreatePromoCodeDto dto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("GENERATE PROMO_CODE : dto {}", dto);
        return ResponseEntity.status(201).body(promoCodeService.generateCode(dto, language));
    }

    @Operation(summary = "LIST OF PROMO_CODE LIST API", description = "Ushbu API barcha promo_code lar ro'yxatini ko'rish uchun ishlatiladi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/view_all_list")
    public ResponseEntity<?> getAllList(@RequestHeader(name = "Accept-Language") Language language) {
        return ResponseEntity.ok(promoCodeService.getAllList(language));
    }

    @Operation(summary = "VERIFICATION PROMO_CODE API", description = "Ushbu API promo+code ni verifikatsiya qilish uchun ishlatiladi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/check_promo_code")
    public ResponseEntity<?> checkPromoCode(@RequestParam String promo_code, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Check Promo-Code : promo_code {}", promo_code);
        var codeDTO = promoCodeService.check_promo_code(promo_code, language);
        return codeDTO.isSuccess() ? ResponseEntity.ok(codeDTO) : ResponseEntity.status(404).body(codeDTO);
    }

}
