package com.example.controller;

import com.example.dto.PromoCodeDto;
import com.example.dto.ResponsePromCode;
import com.example.enums.Language;
import com.example.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * This method is used for generating promo-code If amount ==0 or amount<=0
     * throw NotMatchException
     *
     * @param money    double
     * @param amount   int
     * @param language Language
     * @return ResponseGenerateDto
     */

    // @PreAuthorize(value = "hasRole('ADMIN')")
    // @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/generate")
    @Operation(summary = "GeneratePromoCode API", description = "This API generating promo_code")
    public ResponseEntity<?> generatePromoCode(
            @RequestParam double money, @RequestParam int amount,
            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("generate promo-code : money {},amount {}", money, amount);

        ResponsePromCode promCode = promoCodeService.generateCode(money, amount, language);
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

    // @PreAuthorize(value = "hasRole('ADMIN')")
    //  @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/viewByPage")
    @Operation(summary = "View ALL Promo-code BY Pageable API", description = "This API viewing all promo_code")
    public ResponseEntity<?> getListPromoCodeByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
                                                    @RequestHeader(name = "Accept-Language",
                                                            defaultValue = "UZ") Language language) {
        List<PromoCodeDto> list = promoCodeService.getListPromoCodeByPage(page, size, language);
        return ResponseEntity.ok(list);

    }



    /**
     * This method is used for viewing all the promoce list data  if not found throw EmptyListException
     *
     * @param language Language
     * @return List<PromoCodeDto></>
     */
    // @PreAuthorize(value = "hasRole('ADMIN')")
    // @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/view_all_list")
    @Operation(summary = "View ALL Promo-Code List API", description = "This API for Viewing all the Promo-code")
    public ResponseEntity<?> getAllList(@RequestHeader(name = "Accept-Language") Language language) {
        List<PromoCodeDto> list = promoCodeService.getAllList(language);
        return ResponseEntity.ok(list);
    }


}
