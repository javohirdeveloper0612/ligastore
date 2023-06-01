package com.example.controller;

import com.example.service.AdminMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin_message")
public class AdminMessageController {
    private final AdminMessageService adminMessageService;

    @Autowired
    public AdminMessageController(AdminMessageService adminMessageService) {
        this.adminMessageService = adminMessageService;
    }


    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/message")
    @Operation(summary = "MESSAGE TO ADMIN API", description = "Ushbu API User tomonidan qandaydir product sotib " +
            "olinmoqchi bo'linsa adminga sms junatiladi va shu sms lar to'plamini olish uchun ushbu API ishlatiladi." +
            "Ushbu List larda ACTIVE VA NOT AVTIVE MESSAGE lar turadi Agar active message larni accept qilinsa " +
            "not active ga utadi")
    public ResponseEntity<?> getAdminMessage() {
        return ResponseEntity.ok(adminMessageService.getAllMessage());
    }


    @Operation(summary = "CHECK SMS API", description = "Ushbu API kelgan SMS ni tasdiqlash yoki bekor qilish uchun" +
            " ishlatiladi . Tasdiqlash uchun parametr sifatida ushbu buyurtmaninh ID si  berish suraladi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/accept")
    public ResponseEntity<?> checkAcceptable(@RequestParam Long id) {
        var responseMessage = adminMessageService.checkAcceptable(id);
        return ResponseEntity.ok(responseMessage);
    }

    @Operation(summary = "USER HISTORY API", description = "Ushbu API har bir foydalanuvchining transaksiya tarixinini chiqarib beradi")
    @GetMapping("/history")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getUserHistory() {
        var history = adminMessageService.getUserHistory();
        return ResponseEntity.ok(history);
    }
}
