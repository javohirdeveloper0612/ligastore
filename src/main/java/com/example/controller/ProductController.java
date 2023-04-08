package com.example.controller;

import com.example.dto.CreateProductDto;
import com.example.dto.ResponseProductDto;
import com.example.enums.Language;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@Tag(name = "ProductController")
@Slf4j
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * This method is used for adding new Product
     *
     * @param productDto ProductDto
     * @return ResponseProductDto
     */
    @PostMapping(value = "/add_product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ADD PRODUCT API", description = "This API for adding new Product")
    public ResponseEntity<?> addProduct(@PathVariable Long id, @Valid @ModelAttribute CreateProductDto productDto) {
        log.info("Add Product : productDto {}", productDto);
        ResponseProductDto dto = productService.addProduct(id,productDto);
        return ResponseEntity.status(201).body(dto);
    }
}
