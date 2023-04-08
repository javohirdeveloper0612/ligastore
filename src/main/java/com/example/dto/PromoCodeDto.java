package com.example.dto;

import com.example.entity.ProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDto {

    private Long id;
    private Long promo_code;
    private Integer score;
    private Double money;
}
