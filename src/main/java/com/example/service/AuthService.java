package com.example.service;

import com.example.dto.auth.*;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exception.auth.*;
import com.example.repository.AuthRepository;
import com.example.security.CustomUserDetail;
import com.example.util.JwtUtil;
import com.example.util.MD5;
import com.example.util.TranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final AuthRepository repository;
    private final ResourceBundleService resourceBundleService;
    private final SmsService smsService;


    @Autowired
    public AuthService(AuthRepository repository, ResourceBundleService resourceBundleService, SmsService smsService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
        this.smsService = smsService;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional = repository.findByPhoneUser(phone);
        if (optional.isEmpty()) throw new UsernameNotFoundException("Bad Credentials");
        return new CustomUserDetail(optional.get());
    }


    @Transactional
    public String sendSms(SendSmsDTO dto, Language language) {
        var optional = repository.findByPhoneUser(dto.getPhone());
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            if (entity.getStatus().equals(ProfileStatus.ACTIVE))
                return smsService.sendSms(entity, dto, ProfileStatus.ACTIVE, language);
            else if (entity.getStatus().equals(ProfileStatus.BLOCK))
                throw new ProfileBlockedException(resourceBundleService.getMessage("profile.blocked", language.name()));
            else if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE))
                return smsService.sendSms(entity, dto, ProfileStatus.NOT_ACTIVE, language);
        }
        String smsCode = SmsService.randomSmsCode();
        ProfileEntity profile = new ProfileEntity();
        profile.setPhoneUser(dto.getPhone());
        profile.setSmsTime(LocalDateTime.now());
        profile.setSmsCode(MD5.md5(smsCode));
        profile.setRole(ProfileRole.ROLE_USER);
        profile.setStatus(ProfileStatus.NOT_ACTIVE);
        repository.save(profile);
        SmsService.sendSmsCode(smsService.removePlusSign(dto.getPhone()), smsCode);
        return "Tasdiqlash sms habar yuborildi";

    }


    public ProfileResponseDTO registration(RegistrationDTO dto, Language language) {
        var optional = repository.findById(getUserId());
        if (optional.isEmpty())
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        else if (optional.get().getStatus().equals(ProfileStatus.ACTIVE))
            throw new ProfileAlReadyRegistrationException(resourceBundleService.getMessage("profile.ready.active", language));
        return getDTO(repository.save(getProfile(optional.get(), dto)));
    }


    public ProfileEntity getProfile(ProfileEntity profile, RegistrationDTO dto) {
        profile.setNameUz(dto.getName());
        profile.setNameRu(TranslateUtil.latinToAcrylic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslateUtil.latinToAcrylic(dto.getSurname()));
        profile.setBirthdate(dto.getBirthdate());
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslateUtil.latinToAcrylic(dto.getProfession()));
        profile.setTeam(dto.getTeam());
        profile.setRegion(dto.getRegion());
        profile.setDistrict(dto.getDistrict());
        profile.setPhoneHome(dto.getPhoneHome());
        profile.setStatus(ProfileStatus.ACTIVE);
        profile.setScore(0D);
        return profile;
    }

    public LoginResponseDTO verification(VerificationDTO dto, Language language) {
        var optional = repository.findByPhoneUser(dto.getPhone());
        if (optional.isEmpty())
            throw new PhoneNotExistsException(resourceBundleService.getMessage("phone.not.exists", language.name()));
        var entity = optional.get();
        if (!entity.getSmsCode().equals(MD5.md5(dto.getPassword())))
            throw new PasswordIncorrectException(resourceBundleService.getMessage("password.wrong", language.name()));
        else if (entity.getStatus().equals(ProfileStatus.BLOCK))
            throw new ProfileBlockedException(resourceBundleService.getMessage("profile.blocked", language));
        else if (LocalDateTime.now().isAfter(entity.getSmsTime().plusMinutes(5)))
            throw new SmsTimeOverException(resourceBundleService.getMessage("sms.time.over", language));
        var response = new LoginResponseDTO();
        response.setStatus(entity.getStatus());
        response.setRole(entity.getRole());
        response.setToken(JwtUtil.encode(entity.getPhoneUser(), entity.getRole()));
        return response;
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

    private Long getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (CustomUserDetail) authentication.getPrincipal();
        return user.getId();
    }

}

