package com.example.dto.promocode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePromoCodeDto {

    @NotNull
    private Double money;

    @NotNull
    private Long amount;

    @NotNull
    @NotBlank
    private String model;
}
