package com.example.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotEmpty(message = "name_uz cannot be null or empty")
    private String name_uz;

    @NotEmpty(message = "name_ru cannot be null or empty")
    private String name_ru;

    @NotEmpty(message = "description_uz cannot be null or empty")
    private String description_uz;

    @NotEmpty(message = "description_ru cannot be null or empty")
    private String description_ru;

    @NotEmpty(message = "model cannot be null or empty")
    private String model;

    @NotNull(message = "file cannot be null or empty")
    private MultipartFile file;

    @NotNull(message = "price cannot be null or empty")
    @Positive
    private Double price;


}


