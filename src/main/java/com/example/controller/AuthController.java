package com.example.controller;

import com.example.dto.auth.ProfileResponseDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.SendSmsDTO;
import com.example.dto.auth.VerificationDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        log.info("SEND SMS : dto{}", dto);
        return ResponseEntity.ok(service.sendSms(dto, language));
    }


    @Operation(summary = "VERIFICATION SMS CODE API", description = "Ushbu API Telefon raqamga kelgan sms ni verifikatsiya" + " qilish uchun ishlatiladi")
    @PostMapping("/public/verification")
    public ResponseEntity<?> verification(@Valid @RequestBody VerificationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("VERIFICATION SMS : dto{}", dto);
        return ResponseEntity.ok().body(service.verification(dto, language));
    }

    @Operation(summary = "REGISTER USER API", description = "Ushbu API USER ni registratsiya qilish uchun ishlatiladi")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/registration")
    public ResponseEntity<ProfileResponseDTO> registration(@Valid @RequestBody RegistrationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("REGISTER USER : dto {}", dto);
        return ResponseEntity.ok(service.registration(dto, language));
    }


}
