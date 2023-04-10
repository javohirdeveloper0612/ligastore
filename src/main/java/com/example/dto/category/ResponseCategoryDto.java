package com.example.dto.category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategoryDto {

    private String message;
    private Boolean status;
    private Integer statusCode;

}
