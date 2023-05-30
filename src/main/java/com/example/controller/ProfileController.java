package com.example.controller;

import com.example.dto.auth.UpdateProfileDTO;
import com.example.enums.Language;
import com.example.security.CustomUserDetail;
import com.example.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update Profile", description = "This API for update Profile Details ")
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProfileDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        var response = service.update(dto, getUserId(), language);
        return ResponseEntity.ok().body(response);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "VIEW PROFILE", description = "This API for update Profile Details ")
    @GetMapping("/get")
    public ResponseEntity<?> getById(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        var response = service.getById(getUserId(), language);
        return ResponseEntity.ok(response);
    }


    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getId();
    }
}
