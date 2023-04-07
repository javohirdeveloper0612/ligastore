package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

public class CategoryCreationDTO {

    @NotBlank(message = "nameUz cannot be null")
    private String nameUz;

    @NotBlank(message = "nameRu cannot be null")
    private String nameRu;

    private MultipartFile multipartFile;

    private Long parentcategoryId;
}
