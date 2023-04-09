package com.example.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductDto {

    private Long id;
    String name_uz;
    private String name_ru;
    private String description_uz;
    private String description_ru;
    private Long score;

}
