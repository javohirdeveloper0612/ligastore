package com.example.controller;
import com.example.dto.CategoryCreationDTO;
import com.example.dto.CategoryResponseListDTO;
import com.example.dto.ResponseCategoryDto;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/category")
@Tag(name = "Category-API-Controller")
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
     * @param language language
     * @return responseCategoryDTO
     */

    @PostMapping(value = "/add_category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD_CATEGORY API", description = "This API for adding new Category")
    public ResponseEntity<?> addCategory(@Valid  @ModelAttribute CategoryCreationDTO categoryCreationDTO,
                                         @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        ResponseCategoryDto responseCategoryDto = categoryService.add_category(categoryCreationDTO, language);
        return ResponseEntity.ok(responseCategoryDto);
    }

    /**
     * This method and API are used to get the category list
     *
     * @param language language
     * @return categoryResponseListDTO
     */

    @GetMapping("/public/category_get_list")
    @Operation(summary = "CATEGORY GET LIST", description = "this API for category get list (only ADMIN) ")
    public ResponseEntity<?> categoryGetList(@RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language){

        List<CategoryResponseListDTO> categoryResponseListDTO = categoryService.getCategoryList(language);
        return ResponseEntity.ok().body(categoryResponseListDTO);

    }





}
