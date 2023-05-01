package com.example.controller;

import com.example.dto.brand.BrandDto;
import com.example.dto.brand.ResponseBrandDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.enums.Language;
import com.example.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class for manipulating brand data
 *
 * @author Firdavs Amonov
 * @version 1.0
 */
@RestController
@RequestMapping("/api/brand")
@Slf4j
@Tag(name = "Brand Controller")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * This method is used for adding new Brand
     *
     * @param brandDto BrandDto
     * @return ResponseBrandDto
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/add_brand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD BRAND API", description = "This API for adding new BRAND")
    public ResponseEntity<?> addBrand(@Valid @ModelAttribute BrandDto brandDto) {
        log.info("ADD BRAND : brandDto {}", brandDto);
        ResponseBrandDto responseBrandDto = brandService.addBrand(brandDto);
        return ResponseEntity.status(201).body(responseBrandDto);
    }

    /**
     * This method is used for getting all the brand data
     * if List is empty throw EmptyListException
     *
     * @param language Language
     * @return ResponseBrandDto
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    @GetMapping("/get_all_brand")
    @Operation(summary = "VIEW ALL BRAND API", description = "This API for viewing all brand data")
    public ResponseEntity<?> getAllBrand(@RequestHeader(name = "Accept-Language") Language language) {
        List<ResponseBrandDto> list = brandService.getAllBrand(language);
        return ResponseEntity.ok(list);
    }

    /**
     * This method is used for getting brand data by id
     * if not founded brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param language Language
     * @return ResponseBrandDto
     */

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    @GetMapping("/get_brand_by_id/{brand_id}")
    @Operation(summary = "VIEW BRAND BY ID API", description = "This API for viewing brand data by id")
    public ResponseEntity<?> getBrandById(@PathVariable Long brand_id, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("GET BRAND BY ID  :  brand_id {}", brand_id);
        ResponseBrandDto brand = brandService.getBrandById(brand_id, language);
        return ResponseEntity.ok(brand);
    }

    /**
     * This method is used for editing brand data
     * if not founded brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param brandDto BrandDto
     * @param language Language
     * @return ResponseBrandDto
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping(value = "/edite_brand/{brand_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "EDITE BRAND API", description = "This API for editing BRAND DATA")
    public ResponseEntity<?> editeBrand(@PathVariable Long brand_id,
                                        @ModelAttribute BrandDto brandDto, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("EDITE BRAND : brandDto {}", brandDto);
        ResponseBrandDto editeBrand = brandService.editeBrand(brand_id, language, brandDto);
        return ResponseEntity.ok(editeBrand);
    }

    /**
     * This method is used for deleting brand data
     * if not founded the brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param language Language
     * @return ResponseBrandDto
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete_brand/{brand_id}")
    @Operation(summary = "DELETE BRAND API", description = "This API for deleting brand data")
    public ResponseEntity<?> deleteBrand(@PathVariable Long brand_id, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("DELETE BRAND : brand_id{}", brand_id);
        ResponseMessage responseMessage = brandService.deleteBrand(brand_id, language);
        return ResponseEntity.ok(responseMessage);
    }
}
