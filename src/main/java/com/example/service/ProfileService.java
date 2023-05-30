package com.example.service;

import com.example.dto.auth.ProfileResponseDTO;
import com.example.dto.auth.UpdateProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.repository.AuthRepository;
import com.example.util.TranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
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


    public ProfileResponseDTO update(UpdateProfileDTO dto, Long userId, Language language) {
        var optional = repository.findById(userId);
        if (optional.isEmpty())
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        return getDTO(repository.save(getProfile(optional.get(), dto)));
    }

    public ProfileEntity getProfile(ProfileEntity profile, UpdateProfileDTO dto) {
        profile.setNameUz(dto.getName());
        profile.setNameRu(TranslateUtil.LatinToAcrylic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslateUtil.LatinToAcrylic(dto.getSurname()));
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslateUtil.LatinToAcrylic(dto.getProfession()));
        profile.setRegion(dto.getRegion());
        profile.setDistrict(dto.getDistrict());
        profile.setPhoneHome(dto.getPhoneHome());
        return profile;
    }

    public ProfileResponseDTO getById(Long userId, Language language) {
        var optional = repository.findById(userId);
        if (optional.isEmpty())
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
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
        dto.setRegionRu(TranslateUtil.LatinToAcrylic(entity.getRegion()));
        dto.setDistrictUz(entity.getDistrict());
        dto.setDistrictRu(TranslateUtil.LatinToAcrylic(entity.getDistrict()));
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
            dto.setDistrictRu(TranslateUtil.LatinToAcrylic(entity.getDistrict()));
            dto.setRegionRu(TranslateUtil.LatinToAcrylic(entity.getRegion()));
        }
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());

        return dto;
    }
}
