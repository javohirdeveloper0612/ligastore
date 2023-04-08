package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {

    @NotBlank
    @NotNull
    private String name_uz;
    @NotBlank
    @NotNull
    private String name_ru;

    @NotBlank
    @NotNull
    private String description_uz;

    @NotBlank
    @NotNull
    private String description_ru;

    @NotNull
    private MultipartFile file;

    @NotNull
    private Long score;


}
