package com.example.dto.promocode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPromoCodeDTO {
    private String message;
    private boolean success;
    private int statusCode;
}
