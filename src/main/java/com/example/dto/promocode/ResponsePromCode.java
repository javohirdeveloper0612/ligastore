package com.example.dto.promocode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePromCode {
    private String message;
    private Integer statusCode;
    private Boolean success;
}
