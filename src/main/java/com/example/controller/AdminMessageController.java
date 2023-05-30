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
            "olinmoqchi bo'linsa adminga sms junatiladi va shu sms lar to'plamini olish uchun ushbu API ishlatiladi")
    public ResponseEntity<?> getAdminMessage() {
        return ResponseEntity.ok(adminMessageService.getAllMessage());
    }


    @Operation(summary = "CHECK SMS API", description = "Ushbu API kelgan SMS ni tasdiqlash yoki belor qilish uchun" +
            " ishlatiladi . Tasdiqlash uchun parametr sifatida ushbu userning ID si va Product ning model Raqami " +
            " berish suraladi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/accept")
    public ResponseEntity<?> checkAcceptable(@RequestParam Long user_id, @RequestParam String product_model) {
        var responseMessage = adminMessageService.checkAcceptable(user_id, product_model);
        return ResponseEntity.ok(responseMessage);
    }

    @Operation(summary = "USER HISTORY API", description = "Ushbu API har bir foydalanuvchining sotib olgan mahsulotining tarixinini chiqarib beradi" +
            "Buning uchun siz USER ning ID sini berishingiz talab qilinadi,Chunki har bir userning o'z history si bo'ladi")
    @GetMapping("/history/{user_id}")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getUserHistory(@PathVariable Long user_id) {
        var history = adminMessageService.getUserHistory(user_id);
        return ResponseEntity.ok(history);
    }
}
