package com.example.controller;

import com.example.service.AdminMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin_message")
@Slf4j
public class AdminMessageController {
    private final AdminMessageService adminMessageService;

    @Autowired
    public AdminMessageController(AdminMessageService adminMessageService) {
        this.adminMessageService = adminMessageService;
    }


    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/messages")
    @Operation(summary = "MESSAGE TO ADMIN API", description = "Ushbu API User tomonidan qandaydir product sotib " +
            "olinmoqchi bo'linsa adminga sms junatiladi va shu sms lar to'plamini olish uchun ushbu API ishlatiladi." +
            "Ushbu List larda ACTIVE VA NOT AVTIVE MESSAGE lar turadi ! Agar Admin accept tugmasini bossa Active Message larni " +
            "accepted statusi  true buladi  va NotActive Message ga aylanadi ! Active Message larni " +
            "accepted statusi false buladi ")
    public ResponseEntity<?> getAdminMessage() {
        return ResponseEntity.ok(adminMessageService.getAllMessage());
    }


    @Operation(summary = "CHECK SMS API", description = "Ushbu API kelgan SMS ni tasdiqlash yoki bekor qilish uchun" +
            " ishlatiladi . Tasdiqlash uchun parametr sifatida ushbu buyurtmaning ID si  berish suraladi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/accept")
    public ResponseEntity<?> checkAcceptable(@RequestParam Long id) {
        log.info("checkAcceptable : id {}", id);
        return ResponseEntity.ok(adminMessageService.checkAcceptable(id));
    }

    @Operation(summary = "USER HISTORY API", description = "Ushbu API har bir foydalanuvchining transaksiya tarixinini chiqarib beradi")
    @GetMapping("/history")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getUserHistory() {
        return ResponseEntity.ok(adminMessageService.getUserHistory());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "DELETE MESSAGE API", description = "Ushbu API Adminga borgan message ni delete qilish uchun ishlatiladi")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        log.info("DELETE MESSAGE id {}", id);
        return ResponseEntity.ok(adminMessageService.deleteMessage(id));
    }
}
