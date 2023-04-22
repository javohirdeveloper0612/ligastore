package com.example.controller;

import com.example.dto.jwt.ResponseMessage;
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


    /**
     * This method is used for
     *
     * @return List<></>
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/message")
    @Operation(summary = "Admin Message API", description = "This API for Sending Sms To Admin")
    public ResponseEntity<?> getAdminMessage() {
        return ResponseEntity.ok(adminMessageService.getAllMessage());
    }

    /**
     *
     * @param user_id Long
     * @param product_model String
     * @return
     */
    @Operation(summary = "Check SMS API", description = "This API for checking SMS")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/accept")
    public ResponseEntity<?> checkAcceptable(@RequestParam Long user_id, @RequestParam String product_model) {
        ResponseMessage responseMessage = adminMessageService.checkAcceptable(user_id, product_model);
        return ResponseEntity.ok(responseMessage);
    }
}
