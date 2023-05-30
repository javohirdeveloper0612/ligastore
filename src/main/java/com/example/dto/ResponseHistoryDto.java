package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHistoryDto {

    private Long id;
    private String first_name;
    private String last_name;
    private String phone;
    private String product_model;
    private Long sell_score;
    private Boolean isAccepted;
}
