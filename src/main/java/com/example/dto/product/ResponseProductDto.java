package com.example.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProductDto {

    private Long id;
    String name_uz;
    private String name_ru;
    private String description_uz;
    private String description_ru;
    private Double score;
    private Double price;
    private String model;
    private Boolean isFamous;
    private String photoUrl;

}
