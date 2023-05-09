package com.example.controller;

import com.example.dto.promocode.CreatePromoCodeDto;
import com.example.enums.Language;
import com.example.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Firdavs Amonov
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/promo_code")
@Tag(name = "Promo-code")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }


    /**
     * This method is used for generating promo-code if amount is equals 0 or less than 0
     * throw new NotMatchException if model not found throw ProductNotFoundException
     *
     * @param dto      CreatePromoCodeDto
     * @param language Language
     * @return ResponsePromoCode
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/generate")
    @Operation(summary = "GeneratePromoCode API", description = "This API generating promo_code")
    public ResponseEntity<?> generatePromoCode(@RequestBody CreatePromoCodeDto dto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        var promCode = promoCodeService.generateCode(dto, language);
        return ResponseEntity.status(201).body(promCode);
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
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/viewByPage")
    @Operation(summary = "View ALL Promo-code BY Pageable API", description = "This API viewing all promo_code")
    public ResponseEntity<?> getListPromoCodeByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("getListPromoCodeByPage : page {},size {}", page, size);
        var list = promoCodeService.getListPromoCodeByPage(page, size, language);
        return ResponseEntity.ok(list);

    }

    /**
     * This method is used for viewing all the promoce list data  if not found throw EmptyListException
     *
     * @param language Language
     * @return List<PromoCodeDto></>
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/view_all_list")
    @Operation(summary = "View ALL Promo-Code List API", description = "This API for Viewing all the Promo-code")
    public ResponseEntity<?> getAllList(@RequestHeader(name = "Accept-Language") Language language) {
        var list = promoCodeService.getAllList(language);
        return ResponseEntity.ok(list);
    }


    /**
     * This method is used for checking promo_code if the promo_code and user_id is exist
     * throw new InvalidPromoCodeException If The promo_code is not exist throw new
     * InvalidPromoCodeException
     *
     * @param promo_code long
     * @param language   Language
     * @return CheckPromoCodeDto
     */
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/check_promo_code")
    @Operation(summary = "Check Promo-Code API", description = "This API for checking promo-code")
    public ResponseEntity<?> checkPromoCode(@RequestParam String promo_code, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Check Promo-Code : promo_code {}", promo_code);
        var codeDTO = promoCodeService.check_promo_code(promo_code, language);
        return codeDTO.isSuccess() ? ResponseEntity.ok(codeDTO) : ResponseEntity.status(404).body(codeDTO);
    }


    /**
     * This method is used for getting promo-codeList by ProductModel
     * if list is empty throw new EmptyListException
     *
     * @param model    String
     * @param language Language
     * @return List<ResponsePromoCode></>
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/promo_code_list_by_model")
    @Operation(summary = "Find Promo-Code List By ProductModel", description = "This API for finding" + " PromoCodeList By ProductModel ")
    public ResponseEntity<?> searchPromoCodeListByProductModel(@RequestParam String model, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Find Promo-Code List By ProductModel : model {}", model);
        var list = promoCodeService.findPromoCodeListByProductModel(model, language);
        return ResponseEntity.ok(list);
    }
}
