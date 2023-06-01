package com.example.controller;

import com.example.dto.brand.BrandDto;
import com.example.enums.Language;
import com.example.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/brand")
@Slf4j
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "ADD BRAND API", description = "Ushbu API yangi brand qo'shish uchun ishlatiladi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/add_brand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBrand(@Valid @ModelAttribute BrandDto brandDto) {
        log.info("ADD BRAND : brandDto {}", brandDto);
        return ResponseEntity.status(201).body(brandService.addBrand(brandDto));
    }

    @Operation(summary = "LIST OF BRAND API", description = "Ushbu API barcha Brand lar ro'yxatini ko'rish uchun ishlatiladi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    @GetMapping("/get_all_brand")
    public ResponseEntity<?> getAllBrand(@RequestHeader(name = "Accept-Language") Language language) {
        return ResponseEntity.ok(brandService.getAllBrand(language));
    }

    @Operation(summary = "GET BRAND BY ID API", description = "Ushbu API har bir  brand ni ko'rish uchun ishlatiladi" +
            " Buning uchun sizdan ushbu brandga tegishli ID berish so'raladi Agar ushbu brandga tegishli ID " +
            " topilmasa code=400 , message=Brand topilmadi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    @GetMapping("/get_brand_by_id/{brand_id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long brand_id, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("GET BRAND BY ID  :  brand_id {}", brand_id);
        return ResponseEntity.ok(brandService.getBrandById(brand_id, language));
    }

    @Operation(summary = "EDITE BRAND API", description = "Ushbu API mavjud brandni edite qilish uchun ishlatiladi" +
            "Agar ushbu brend ga tegishli ID topilmasa code=400 message=Brand topilmadi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping(value = "/edite_brand/{brand_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editeBrand(@PathVariable Long brand_id, @ModelAttribute BrandDto dto, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("EDITE BRAND : dto {}", dto);
        return ResponseEntity.ok(brandService.editeBrand(brand_id, language, dto));
    }

    @Operation(summary = "DELETE BRAND API", description = "Ushbu API mavjud bend ni o'chirish uchun ishlatiladi" +
            "Buning uchun sizdan shu brendga tegishli ID ni berish so'raladi Agar brend ga tegishli ID toplimasa " +
            "code = 400 message=Brand topilmadi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete_brand/{brand_id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long brand_id, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("DELETE BRAND : brand_id{}", brand_id);
        return ResponseEntity.ok(brandService.deleteBrand(brand_id, language));
    }
}
