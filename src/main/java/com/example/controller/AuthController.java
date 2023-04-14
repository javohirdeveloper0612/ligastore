package com.example.controller;

import com.example.dto.auth.*;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "AuthController")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Login Method", description = "this method for registration")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {
        LoginResponseDTO response = service.login(dto, Language.UZ);
        log.info(" login method dtoUsername {}", dto.getUsername());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Phone Registration", description = "This API for send message Phone number")
    @PostMapping("/send_sms")
    public ResponseEntity<String> sendSms(@Valid @RequestBody SendSmsDTO dto) {
        String response = service.sendSms(dto, Language.UZ);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verification", description = "This API for verification ")
    @PostMapping("/verification")
    public ResponseEntity<?> verification(@Valid @RequestBody VerificationDTO dto) {
        ProfileResponseDTO response = service.verification(dto, Language.UZ);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Method for registration", description = "This method used to create a user")
    @PostMapping("/registration/{id}")
    private ResponseEntity<ProfileResponseDTO> registration(@Valid @RequestBody RegistrationDTO dto,
                                                            @PathVariable(name = "id") Long id,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        ProfileResponseDTO result = service.registration(dto, id, language);
        return ResponseEntity.ok(result);
    }

}
