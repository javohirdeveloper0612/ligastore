package com.example.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCategoryDto {

    private Long id;
    private String nameUz;
    private String nameRu;
    private String photoUrl;
    private Long parentCategoryId;


}
