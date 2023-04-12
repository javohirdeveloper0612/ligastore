package com.example.dto.promocode;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePromoCodeDto {

    private Long id;
    private Long promo_code;
    private Integer score;
    private Double money;
    private String product_model;
}
