package com.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EditeCategoryDto {


    @NotBlank(message = "nameUz cannot be null")
    private String nameUz;

    @NotBlank(message = "nameRu cannot be null")
    private String nameRu;

    @NotNull
    private MultipartFile multipartFile;

}
