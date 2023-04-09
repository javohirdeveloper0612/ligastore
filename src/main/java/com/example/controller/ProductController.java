package com.example.controller;
import com.example.dto.product.ProductDto;
import com.example.dto.product.ResponseProductDto;
import com.example.enums.Language;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "ProductController")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * This method is used for converting CreatedDto to ProductEntity
     * If Category not Found throw new NotFoundParentCategoryException
     *
     * @param productDto ProductDto
     * @return ProductEntity
     */
    @PostMapping(value = "/add_product/{category_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD PRODUCT API", description = "This API for adding new Product")
    public ResponseEntity<?> addProduct(@PathVariable Long category_id, @Valid @ModelAttribute ProductDto productDto,
                                        @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Add Product : productDto {}", productDto);
        ResponseProductDto dto = productService.addProduct(category_id, productDto, language);
        return ResponseEntity.status(201).body(dto);

    }

    /**
     * This method is used for editing product data if product_id not founded
     * throw new ProductNotFoundException
     *
     * @param product_id Long
     * @param productDto ProductDto
     * @param language   Language
     * @return ResponseProductDto
     */
    @PutMapping(value = "/edite/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Edite Product API", description = "This API for editing Product Data")
    public ResponseEntity<?> editeProduct(@PathVariable Long product_id, @Valid @ModelAttribute ProductDto productDto,
                                          @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Edite Product :  productDto {} ", productDto);
        ResponseProductDto dto = productService.editeProduct(product_id, productDto, language);
        return ResponseEntity.ok(dto);

    }


    /**
     * This method is used for viewing all the product data list if product list is empty
     * throw EmptyListException
     *
     * @param language Language
     * @return List<ResponseProductDto></>
     */
    @GetMapping("/view_product_list/{category_id}")
    @Operation(summary = "View Product Data List API", description = "This API for viewing all the productDto")
    public ResponseEntity<?> getProductList(@PathVariable Long category_id,
                                            @RequestHeader(name = "Accept-Language") Language language) {
        List<ResponseProductDto> productList = productService.getProductList(category_id, language);
        return ResponseEntity.ok(productList);
    }




}
