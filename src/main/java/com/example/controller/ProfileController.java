package com.example.controller;

import com.example.dto.auth.UpdateProfileDTO;
import com.example.enums.Language;
import com.example.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "EDITE PROFILE API", description = "Ushbu API har bir user o'z profilini  edite qilish uchun ishlatiladi")
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProfileDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("EDITE PROFILE : dto{}", dto);
        return ResponseEntity.ok().body(service.update(dto, language));
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "VIEW PROFILE API", description = "Ushbu API har bir user o'z profilini ko'rish uchun ishlatiladi ")
    @GetMapping("/get")
    public ResponseEntity<?> getById(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        return ResponseEntity.ok(service.getById(language));
    }


}
