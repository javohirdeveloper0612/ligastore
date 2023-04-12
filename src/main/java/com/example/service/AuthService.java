package com.example.service;

import com.example.dto.LoginDTO;
import com.example.dto.LoginResponseDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileStatus;
import com.example.exception.auth.ProfileBlockedException;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.repository.AuthRepository;
import com.example.security.CustomUserDetail;
import com.example.util.JwtUtil;
import com.example.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final AuthRepository repository;
    private final ResourceBundleService resourceBundleService;

    @Autowired
    public AuthService(AuthRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional = repository.findByUsername(username);
        if (optional.isEmpty()) throw new UsernameNotFoundException("Bad Credentials");
        return new CustomUserDetail(optional.get());
    }


    public LoginResponseDTO login(LoginDTO dto, Language language) {

        Optional<ProfileEntity> optional = repository.findByUsernameAndPassword(dto.getUsername(), MD5.md5(dto.getPassword()));
        if (optional.isEmpty())
            throw new ProfileNotFoundException(resourceBundleService.getMessage("admin.not.found", language.name()));

        ProfileEntity entity = optional.get();

        if (entity.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new ProfileBlockedException(resourceBundleService.getMessage("profile.blocked", language.name()));
        }
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setName(entity.getNameUz());
        responseDTO.setUsername(entity.getUsername());
        responseDTO.setRole(entity.getRole());
        responseDTO.setToken(JwtUtil.encode(entity.getUsername(), entity.getRole()));

        return responseDTO;
    }


}
