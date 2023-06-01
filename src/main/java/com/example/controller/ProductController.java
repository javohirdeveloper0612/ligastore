package com.example.controller;


import com.example.dto.product.ProductDto;
import com.example.enums.Language;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "ADD PRODUCT API", description = "Ushbu API yangi product qo'shish uchun ishlatiladi va qaysi " +
            " category ga biriktirmoqchi bo'lsangiz o'sha category ni ID raqami berish  so'raladi Agar category topilmasa" +
            " code=400 message=category topilmadi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/add_product/{category_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@PathVariable Long category_id, @Valid @ModelAttribute ProductDto productDto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Add Product : productDto {}", productDto);
        return ResponseEntity.status(201).body(productService.addProduct(category_id, productDto, language));
    }

    @Operation(summary = "EDITE PRODUCT API", description = "Ushbu API product ni edite qilish uchun ishlatiladi " +
            " va buning uchun sizdan productga tegishli ID raqam ni berish so'raladi Agar ID raqam topilmasa code=400" +
            " message=product topilmadi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/edite/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editeProduct(@PathVariable Long product_id, @Valid @ModelAttribute ProductDto productDto, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Edite Product :  productDto {} ", productDto);
        return ResponseEntity.ok(productService.editeProduct(product_id, productDto, language));
    }

    @Operation(summary = "LIST OF PRODUCT API", description = "Ushbu API barcha product lar ro'yxatini ko'rish uchun ishlatiladi" +
            " va sizdan ushbu productlar qaysi category ga kirsa usha categoryning ID raqami berish so'raladi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/view_product_list/{category_id}")
    public ResponseEntity<?> getProductList(@PathVariable Long category_id, @RequestHeader(name = "Accept-Language") Language language) {
        log.info("getProductList : category_id{}", category_id);
        return ResponseEntity.ok(productService.getProductList(category_id, language));
    }


    @Operation(summary = "VIEW PRODUCT API", description = "Ushbu API har bir product ni ko'rish uchun ishlatiladi va sizdan productga" +
            " tegishli ID raqam ni berish so'raladi Agar product topilmasa code=400 va message = product topilmadi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/view_product_by_id/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable Long product_id, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("View Product BY ID : product_id {}", product_id);
        return ResponseEntity.ok(productService.getProductById(product_id, language));
    }


    @Operation(summary = "DELETE PRODUCT API", description = "Ushbu API product ni delete qilish uchun ishlatialdi" +
            " va sizdan productga tegishli ID raqam berish suraladi Agar ID raqam topilmasa code=400 va message=" +
            "product topilmadi degan xabar ni olasiz !")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/deleteProduct/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long product_id, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Delete Product : product_id{}", product_id);
        return ResponseEntity.ok(productService.deleteProduct(product_id, language));
    }

    @Operation(summary = "SELL PRODUCT API", description = "Ushbu API Agar User product ni sotip olmoqchi bo'lsa " +
            "ushbu API ga murojat qiladi va buning uchun sizdan product ninh modelini berish soraladi Agar product modeli " +
            " topilmasa code=400 va message product topilmadi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @PostMapping("/sell_product")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> sellProduct(@RequestParam String product_model, @RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Sell product :  model_product {}", product_model);
        var responseMessage = productService.sellProduct(product_model, language);
        return responseMessage.isSuccess() ? ResponseEntity.ok(responseMessage) : ResponseEntity.status(400).body(responseMessage);
    }


    @Operation(summary = "LIST OF PRODUCT API", description = "Ushbu API barcha category larga tegishli  productlar ro'yxatini ko'rish uchun ishlatiladi")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get_all_product")
    public ResponseEntity<?> getALLProduct(@RequestHeader(name = "Accept-Language", defaultValue = "UZ") Language language) {
        return ResponseEntity.ok(productService.getAllProduct(language));
    }

}
