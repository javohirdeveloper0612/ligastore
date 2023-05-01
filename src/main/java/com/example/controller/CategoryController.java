package com.example.controller;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.EditeCategoryDto;
import com.example.dto.category.ResponseCategoryDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class for manipulating category data
 * @author Firdavs Amonov
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/category")
@Tag(name = "Category Controller")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /**
     * this method and API are used to create category and ParentCategory
     *
     * @param categoryCreationDTO ResponseCategoryDTO
     * @param language            language
     * @return result
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/add_category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD_CATEGORY API", description = "This API for adding new Category")
    public ResponseEntity<?> addCategory(@Valid
                                         @ModelAttribute CategoryDto categoryCreationDTO,
                                         @RequestHeader(name = "Accept-Language", defaultValue = "UZ")
                                         Language language) {
        log.info("Creation Category : categoryCreationDTO {} ", categoryCreationDTO);
        ResponseCategoryDto result = categoryService.add_category(categoryCreationDTO, language);
        return ResponseEntity.ok(result);
    }

    /**
     * This method and API are used to get the category list
     *
     * @param language language
     * @return result
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @GetMapping("/get_all_category/{brand_id}")
    @Operation(summary = "CATEGORY  LIST", description = "This API is used for getting category list  ")
    public ResponseEntity<?> getAllCategory(
            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language,
            @PathVariable Long brand_id) {
        log.info("Getting category list : brand_id {}", brand_id);
        List<ResponseCategoryDto> result = categoryService.getCategoryList(brand_id, language);
        return ResponseEntity.ok().body(result);
    }


    /**
     * this method and API will find the category by id
     *
     * @param id       Long
     * @param language language
     * @return result
     */
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get_category_by_id/{id}")
    @Operation(summary = "Get Category By ID API", description = "This API is used  category get by id ( public )")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id,
                                             @RequestHeader(name = "Accept-Language", defaultValue = "UZ")
                                             Language language) {
        log.info("getCategoryById : id {}", id);
        ResponseCategoryDto result = categoryService.getCategoryById(id, language);
        return ResponseEntity.ok().body(result);

    }

    /**
     * this method and API are used to replace the category finder by id
     *
     * @param id                Long
     * @param categoryUpdateDTO ccategoryUpdateDTO
     * @param language          language
     * @return result;
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/edite_category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "CATEGORY UPDATE BY ID", description = "This API is used for editing Category")
    public ResponseEntity<?> editeCategory(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute EditeCategoryDto categoryUpdateDTO,
            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Edite Category : id {},categoryUpdateDTO {}", id, categoryUpdateDTO);
        ResponseCategoryDto result = categoryService.editeCategory(id, categoryUpdateDTO, language);
        return ResponseEntity.ok().body(result);
    }

    /**
     * this method and API is used to search the category by id and remove it from the database
     *
     * @param id       Long
     * @param language language
     * @return String result
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "CATEGORY DELETE BY ID", description = "this API category update by id (only ADMIN) ")
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable("id") Long id,
            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("deleteCategory : id {}", id);
        ResponseMessage responseMessage = categoryService.deleteCategory(id, language);
        return ResponseEntity.ok().body(responseMessage);

    }

    /**
     * this method and API are used to paginate the category
     *
     * @param page     int
     * @param size     int
     * @param language language
     * @return allCategory
     */
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "CATEGORY GET LIST PAGINATION", description = "this API category list pagination (only ADMIN va USER) ")
    @GetMapping("/get_category_list_by_page")
    public ResponseEntity<?> getCategoryByPage(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "1") int size,
                                               @RequestHeader(name = "Accept-Language", defaultValue = "UZ")
                                               Language language) {
        log.info("categoryGetPages : page {},size{}", page, size);
        Page<ResponseCategoryDto> allCategory = categoryService.categoryGetPaginationList(page, size, language);
        return ResponseEntity.ok().body(allCategory);

    }

}
