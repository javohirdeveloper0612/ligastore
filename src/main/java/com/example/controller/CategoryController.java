package com.example.controller;
import com.example.dto.CategoryCreationDTO;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/category")
@Tag(name = "Category-API-Controller")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * This method is used to create a category
     *
     * @param dto CategoryCreationDTO
     * @param language language
     * @return result
     */


    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create-Category", description = "this method for create new category")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@Valid @ModelAttribute CategoryCreationDTO dto,
                                    @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        CategoryCreationDTO result = categoryService.create(dto,language);
        return ResponseEntity.ok().body(result);

    }

}
