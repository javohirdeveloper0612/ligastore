package com.example.dto.promocode;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePromoCodeDto {

    private Long id;
    private String promo_code;
    private double score;
    private String product_model;
}
