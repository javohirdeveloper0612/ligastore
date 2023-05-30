package com.example.controller;

import com.example.dto.auth.*;
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
@Tag(name = "AuthController")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }


    /**
     * This method is used for registration phone  If phone is exist throw PhoneAlreadyException
     *
     * @param dto SendSmsDto
     * @return String
     */
    @Operation(summary = "Phone Registration", description = "This API for send message Phone number")
    @PostMapping("/public/send_sms")
    public ResponseEntity<String> sendSms(@Valid @RequestBody SendSmsDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        String response = service.sendSms(dto, language);
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used for Verification sms Code if not founded phone throw PhoneNotFoundException
     * If not found password throw PasswordNotFoundException
     *
     * @param dto VerificationDTO
     * @return ProfileResponseDto
     */
    @Operation(summary = "Verification", description = "Ushbu APIdan status NOT_ACTIVE bulsa registrationga utqaziladi yoki Status ACTIVE bulsa Avval ruyxatdan utgan buladi " +
            "shunchaki tokenni saqlab applicationga utadigan qilinadi tamom !")
    @PostMapping("/public/verification")
    public ResponseEntity<?> verification(@Valid @RequestBody VerificationDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        LoginResponseDTO responseDTO = service.verification(dto, language);
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * This method is used for User registration if profile's data is not found
     * throw ProfileNotFoundException
     *
     * @param dto      RegistrationDto
     * @param language Language
     * @return ProfileResponseDto
     */
    @Operation(summary = "Method for registration", description = "This method used to create a user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/registration")
    public ResponseEntity<ProfileResponseDTO> registration(@Valid @RequestBody RegistrationDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        ProfileResponseDTO result = service.registration(getUserId(), dto, language);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Login for ADMIN",description = "Login with username and password")
    @GetMapping("/public/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        var responseDTO = service.login(dto, language);
        return ResponseEntity.ok().body(responseDTO);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getId();
    }

}
