package com.example.service;

import com.example.dto.auth.ProfileResponseDTO;
import com.example.dto.auth.UpdateProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.repository.AuthRepository;
import com.example.security.CustomUserDetail;
import com.example.util.TranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final AuthRepository repository;
    private final ResourceBundleService resourceBundleService;

    @Autowired
    public ProfileService(AuthRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }


    public ProfileResponseDTO update(UpdateProfileDTO dto, Language language) {
        var optional = repository.findById(getUserId());
        if (optional.isEmpty()) throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        return getDTO(repository.save(getProfile(optional.get(), dto)));
    }

    public ProfileEntity getProfile(ProfileEntity profile, UpdateProfileDTO dto) {
        profile.setNameUz(dto.getName());
        profile.setNameRu(TranslateUtil.latinToAcrylic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslateUtil.latinToAcrylic(dto.getSurname()));
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslateUtil.latinToAcrylic(dto.getProfession()));
        profile.setRegion(dto.getRegion());
        profile.setDistrict(dto.getDistrict());
        profile.setPhoneHome(dto.getPhoneHome());
        return profile;
    }

    public ProfileResponseDTO getById(Language language) {
        var optional = repository.findById(getUserId());
        if (optional.isEmpty()) throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        return getDTOByLang(optional.get(), language);
    }

    public ProfileResponseDTO getDTO(ProfileEntity entity) {
        var dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setSurnameRu(entity.getSurnameRu());
        dto.setSurnameUz(entity.getSurnameUz());
        dto.setProfessionUz(entity.getProfessionUz());
        dto.setProfessionRu(entity.getProfessionRu());
        dto.setRegionUz(entity.getRegion());
        dto.setRegionRu(TranslateUtil.latinToAcrylic(entity.getRegion()));
        dto.setDistrictUz(entity.getDistrict());
        dto.setDistrictRu(TranslateUtil.latinToAcrylic(entity.getDistrict()));
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());
        return dto;
    }

    public ProfileResponseDTO getDTOByLang(ProfileEntity entity, Language language) {
        var dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        if (language.equals(Language.UZ)) {
            dto.setNameUz(entity.getNameUz());
            dto.setSurnameUz(entity.getSurnameUz());
            dto.setProfessionUz(entity.getProfessionUz());
            dto.setRegionUz(entity.getRegion());
            dto.setDistrictUz(entity.getDistrict());

        } else if (language.equals(Language.RU)) {
            dto.setNameRu(entity.getNameRu());
            dto.setSurnameRu(entity.getSurnameRu());
            dto.setProfessionRu(entity.getProfessionRu());
            dto.setDistrictRu(TranslateUtil.latinToAcrylic(entity.getDistrict()));
            dto.setRegionRu(TranslateUtil.latinToAcrylic(entity.getRegion()));
        }
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());
        return dto;
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getId();
    }
}
