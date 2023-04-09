package com.example.dto.category;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CategoryUpdateDTO {


    @NotBlank(message = "nameUz cannot be null")
    private String nameUz;

    @NotBlank(message = "nameRu cannot be null")
    private String nameRu;

}
