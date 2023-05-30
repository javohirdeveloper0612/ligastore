package com.example.controller;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.EditeCategoryDto;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * This class for manipulating category data
 *
 * @author Firdavs Amonov
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "ADD_CATEGORY API", description = "Ushbu API har bir brend ga category qo'shish uchun ishlatiladi" +
            " va qaysi brend ga category qo'shmoqchi bo'lsangiz o'sha brend ni ID raqami berishingiz so'raladi" +
            "Agar brend topilmasa code =400 message = brend topilmadi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/add_category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCategory(@Valid @ModelAttribute CategoryDto dto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language lan) {
        log.info("Creation Category : categoryCreationDTO {} ", dto);
        var result = categoryService.add_category(dto, lan);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "LIST OF CATEGORY API", description = "Ushbu API barcha category lar ro'yxatini ko'rish " +
            "uchun ishlatiladi va buning uchun qaysi brend ga tegishli category ni ko'rmoqchi bo'sangiz o'sha brend ni " +
            "ID raqamini berishingiz so'raladi Agar brend ga tegisgli ID raqami topilmasa code=400 message = brend " +
            "topilmadi degan xabar olasiz")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @GetMapping("/get_all_category/{brand_id}")
    public ResponseEntity<?> getAllCategory(@RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language, @PathVariable Long brand_id) {
        log.info("Getting category list : brand_id {}", brand_id);
        var result = categoryService.getCategoryList(brand_id, language);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "VIEW CATEGORY", description = "Ushbu API har bir category ni ID raqami bo'yicha ko'rish uchun ishlatiladi" +
            " Buning uchun sizdan Category ning ID raqami berish  so'raladi Agar category topilmasa code=400" +
            " message=Category topilmadi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get_category_by_id/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("getCategoryById : id {}", id);
        var result = categoryService.getCategoryById(id, language);
        return ResponseEntity.ok().body(result);

    }

    @Operation(summary = "EDITE CATEGORY API", description = "Ushbu API har bir category ni edite qilish uchun ishlatiladi" +
            " buning uchun category ni ID raqamini  berish so'raladi Agar ID raqam topilmasa code=400 message=category topilmadi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/edite_category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editeCategory(@PathVariable("id") Long id, @Valid @ModelAttribute EditeCategoryDto dto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language lan) {
        log.info("Edite Category : id {},categoryUpdateDTO {}", id, dto);
        var result = categoryService.editeCategory(id, dto, lan);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "DELETE CATEGORY API", description = "Ushbu API har bir category ni o'chirish uchun ishlatiladi" +
            " va buning uchun sizdan category tegishli ID raqamini berish so'raladi Agar ID raqam topilmasa code=400" +
            " message=category topilmadi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("deleteCategory : id {}", id);
        var responseMessage = categoryService.deleteCategory(id, language);
        return ResponseEntity.ok().body(responseMessage);
    }


}
