package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminMessage {
    private Long id;
    private String first_name;
    private String last_name;
    private String phone;
    private String product_name;
    private String product_model;
    private Boolean accepted;

}
