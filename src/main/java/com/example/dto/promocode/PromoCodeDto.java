package com.example.dto.promocode;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDto {

    private Long id;
    private Long promo_code;
    private Integer score;
    private Double money;
}
