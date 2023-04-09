package com.example.controller;

import com.example.dto.category.CategoryCreationDTO;
import com.example.dto.category.CategoryResponseListDTO;
import com.example.dto.category.CategoryUpdateDTO;
import com.example.dto.category.ResponseCategoryDto;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
     * @param language            language
     * @return result
     */

    @PostMapping(value = "/add_category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD_CATEGORY API", description = "This API for adding new Category")
    public ResponseEntity<?> addCategory(@Valid
                                         @ModelAttribute CategoryCreationDTO categoryCreationDTO,
                                         @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        ResponseCategoryDto result = categoryService.add_category(categoryCreationDTO, language);
        return ResponseEntity.ok(result);
    }

    /**
     * This method and API are used to get the category list
     *
     * @param language language
     * @return result
     */

    @GetMapping("/public/category_get_list")
    @Operation(summary = "CATEGORY GET LIST", description = "this API for category get list (only ADMIN) ")
    public ResponseEntity<?> categoryGetList(@Valid
                                             @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        List<CategoryResponseListDTO> result = categoryService.getCategoryList(language);
        return ResponseEntity.ok().body(result);

    }

    /**
     * this method and API will find the category by id
     *
     * @param id       Long
     * @param language language
     * @return result
     */

    @GetMapping("/public/getById/{id}")
    @Operation(summary = "CATEGORY GET BY ID", description = "this API category get by id ( public )")
    public ResponseEntity<?> categoryGetById(@Valid
                                             @PathVariable("id") Long id,
                                             @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        CategoryResponseListDTO result = categoryService.getCategoryById(id, language);
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

    @PutMapping("/updatebyid/{id}")
    @Operation(summary = "CATEGORY UPDATE BY ID", description = "this API category update by id ( only ADMIN) ")
    public ResponseEntity<?> categoryUpdateById(@Valid
                                                @PathVariable("id") Long id,
                                                @RequestBody CategoryUpdateDTO categoryUpdateDTO,
                                                @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {

        CategoryUpdateDTO result = categoryService.categoryUpdate(id, categoryUpdateDTO, language);
        return ResponseEntity.ok().body(result);

    }

    /**
     * this method and API is used to search the category by id and remove it from the database
     *
     * @param id       Long
     * @param language language
     * @return String result
     */

    @Operation(summary = "CATEGORY DELETE BY ID", description = "this API category update by id (only ADMIN) ")
    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<?> categoryDeleteByid(@Valid
                                                @PathVariable("id") Long id,
                                                @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {


        String result = categoryService.categoryDelete(id, language);
        return ResponseEntity.ok().body(result);

    }

    /**
     * this method and API are used to paginate the category
     *
     * @param page int
     * @param size int
     * @param language language
     * @return allCategory
     */

    @Operation(summary = "CATEGORY GET LIST PAGINATION",description = "this API category list pagination (only ADMIN va USER) ")
    @GetMapping("/public/categorygetlistpagination")
    public ResponseEntity<?> categoryGetPages(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "1") int size,
                                              @RequestHeader(name = "Accept-Language",defaultValue = "UZ") Language language){

        Page<CategoryResponseListDTO> allCategory = categoryService.categoryGetPaginationList(page,size,language);
        return ResponseEntity.ok().body(allCategory);

    }


}
