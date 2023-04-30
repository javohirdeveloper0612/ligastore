package com.example.service;

import com.example.dto.auth.ProfileResponseDTO;
import com.example.dto.auth.UpdateProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.repository.AuthRepository;
import com.example.util.TranslaterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Optional<ProfileEntity> optional = repository.findById(userId);
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        }


        ProfileEntity profile = optional.get();
        profile.setNameUz(dto.getName());
        profile.setNameRu(TranslaterUtil.latinToCryllic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslaterUtil.latinToCryllic(dto.getSurname()));
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslaterUtil.latinToCryllic(dto.getProfession()));
        profile.setRegion(dto.getRegion());
        profile.setDistrict(dto.getDistrict());
        profile.setPhoneHome(dto.getPhoneHome());

        repository.save(profile);


        return getDTO(profile);
    }

    public ProfileResponseDTO getById(Long userId, Language language) {
        Optional<ProfileEntity> optional = repository.findById(userId);
        if (optional.isEmpty())
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));


        return getDTOByLang(optional.get(),language);
    }

    public ProfileResponseDTO getDTO(ProfileEntity entity) {

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setSurnameRu(entity.getSurnameRu());
        dto.setSurnameUz(entity.getSurnameUz());
        dto.setProfessionUz(entity.getProfessionUz());
        dto.setProfessionRu(entity.getProfessionRu());
        dto.setRegionUz(entity.getRegion());
        dto.setRegionRu(TranslaterUtil.latinToCryllic(entity.getRegion()));
        dto.setDistrictUz(entity.getDistrict());
        dto.setDistrictRu(TranslaterUtil.latinToCryllic(entity.getDistrict()));
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());


        return dto;
    }
    public ProfileResponseDTO getDTOByLang(ProfileEntity entity,Language language) {

        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setId(entity.getId());
        if (language.equals(Language.UZ)){
            dto.setNameUz(entity.getNameUz());
            dto.setSurnameUz(entity.getSurnameUz());
            dto.setProfessionUz(entity.getProfessionUz());
            dto.setRegionUz(entity.getRegion());
            dto.setDistrictUz(entity.getDistrict());

        }else if (language.equals(Language.RU)){
            dto.setNameRu(entity.getNameRu());
            dto.setSurnameRu(entity.getSurnameRu());
            dto.setProfessionRu(entity.getProfessionRu());
            dto.setDistrictRu(TranslaterUtil.latinToCryllic(entity.getDistrict()));
            dto.setRegionRu(TranslaterUtil.latinToCryllic(entity.getRegion()));
        }
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());

        return dto;
    }
}
