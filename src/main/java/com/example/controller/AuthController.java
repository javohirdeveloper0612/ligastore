package com.example.controller;

import com.example.dto.auth.LoginDTO;
import com.example.dto.auth.LoginResponseDTO;
import com.example.dto.auth.VerificationDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info(" login method dtoUsername {}" ,dto.getUsername());
        return ResponseEntity.ok().body(response);
    }

@Operation(summary = "Phone Registration",description = "This API for send message Phone number")
    @PostMapping("/send_sms")
    public ResponseEntity<?> verification(@Valid @RequestBody VerificationDTO dto){
        return service.verification(dto,Language.UZ);
}

}
