package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminMessage {
    private Long OrderId;
    private String user_name;
    private String user_surname;
    private String phone;
    private String product_name;
    private String product_model;

}
