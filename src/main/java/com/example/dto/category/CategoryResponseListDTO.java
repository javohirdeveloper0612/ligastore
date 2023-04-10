package com.example.dto.category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseListDTO {

    private Long id;
    private String name_uz;
    private String name_ru;
    private String photo_url;


}
