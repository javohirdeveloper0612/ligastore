package com.example.controller;


import com.example.dto.ResponseMessage;

import com.example.dto.product.ProductDto;
import com.example.dto.product.ResponseProductDto;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.security.CurrentUser;
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

    // @PreAuthorize(value = "hasRole('ADMIN')")
    // @SecurityRequirement(name = "Bearer Authentication")
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

    // @PreAuthorize(value = "hasRole('ADMIN')")
    // @SecurityRequirement(name = "Bearer Authentication")
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
    @GetMapping("/public/view_product_list/{category_id}")
    @Operation(summary = "View Product Data List API", description = "This API for viewing all the productDto")
    public ResponseEntity<?> getProductList(@PathVariable Long category_id,
                                            @RequestHeader(name = "Accept-Language") Language language) {
        List<ResponseProductDto> productList = productService.getProductList(category_id, language);
        return ResponseEntity.ok(productList);
    }

    /**
     * This method is used for viewing all the product data list if product list is empty
     * throw EmptyListException
     *
     * @param language Language
     * @return List<ResponseProductDto></>
     */

    @GetMapping("/public/view_product_list_by_page/{category_id}")
    @Operation(summary = "View_product_List By Page API", description = "This Api for viewing all the product data by page")
    public ResponseEntity<?> getProductListByPage(@PathVariable Long category_id, @RequestParam int page, @RequestParam int size,
                                                  @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        List<ResponseProductDto> list = productService.getProductListByPage(page, size, language, category_id);
        return ResponseEntity.ok(list);
    }


    /**
     * This method is used for viewing product data by Id
     * If not found product_id throw ProductNotFoundException
     *
     * @param product_id Long
     * @param language   Language
     * @return ResponseProductDto
     */
    @GetMapping("/public/view_product_by_id/{product_id}")
    @Operation(summary = "View Product By Id API", description = "This API for viewing product data by Id")
    public ResponseEntity<?> getProductById(@PathVariable Long product_id,
                                            @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("View Product BY ID : product_id {}", product_id);
        ResponseProductDto product = productService.getProductById(product_id, language);
        return ResponseEntity.ok(product);
    }


    /**
     * This method is used for deleting product data if not found product_id
     * throw new ProductNotFoundException
     *
     * @param product_id Long
     * @param language   Language
     * @return ResponseMessage
     */

    // @PreAuthorize(value = "hasRole('ADMIN')")
    // @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/deleteProduct/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long product_id,
                                           @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Delete Product : product_id{}", product_id);
        ResponseMessage product = productService.deleteProduct(product_id, language);
        return ResponseEntity.ok(product);
    }

    /**
     * This method is used for selling product
     *
     * @param user  ProfileEntity
     * @param score Long
     * @return ResponseMessage
     */
    @PostMapping("public/sell_product")
    @Operation(summary = "Sell Product API", description = "This API for selling Product")
    public ResponseEntity<?> sellProduct(@CurrentUser ProfileEntity user, @RequestParam Long score) {
        log.info("Sell product : score {}, user {}", score, user);
        ResponseMessage responseMessage = productService.sellProduct(user, score);
        return responseMessage.isSuccess() ? ResponseEntity.ok(responseMessage) : ResponseEntity.status(400).body(responseMessage);
    }
}
