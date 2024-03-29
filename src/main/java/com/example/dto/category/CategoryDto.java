package com.example.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotEmpty(message = "nameUz cannot be null")
    private String nameUz;

    @NotEmpty(message = "nameRu cannot be null")
    private String nameRu;

    @NotNull(message = "file cannot be null")
    private MultipartFile multipartFile;

    @NotNull(message = "brandId cannot be null")
    private Long brandId;
}
