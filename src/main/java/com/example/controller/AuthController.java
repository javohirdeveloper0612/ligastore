package com.example.controller;

import com.example.dto.auth.ProfileResponseDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.SendSmsDTO;
import com.example.dto.auth.VerificationDTO;
import com.example.enums.Language;
import com.example.security.CustomUserDetail;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }


    @Operation(summary = "SEND SMS TO PHONE API", description = "Ushbu API telefon raqamga sms junatish uchun ishlatiladi")
    @PostMapping("/public/send_sms")
    public ResponseEntity<String> sendSms(@Valid @RequestBody SendSmsDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        String response = service.sendSms(dto, language);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "VERIFICATION SMS CODE API", description = "Ushbu API Telefon raqamga kelgan sms ni verifikatsiya" + " qilish uchun ishlatiladi")
    @PostMapping("/public/verification")
    public ResponseEntity<?> verification(@Valid @RequestBody VerificationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        var responseDTO = service.verification(dto, language);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "REGISTER USER API", description = "Ushbu API USER ni registratsiya qilish uchun ishlatiladi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/registration")
    public ResponseEntity<ProfileResponseDTO> registration(@Valid @RequestBody RegistrationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        var result = service.registration(dto, language);
        return ResponseEntity.ok(result);
    }




}
