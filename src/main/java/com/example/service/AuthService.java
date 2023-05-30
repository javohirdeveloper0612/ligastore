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
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    private final static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM4MjcsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6MzgyNywibmFtZSI6Ik9PTyBPc29iZW5ubyIsImVtYWlsIjoidW1pZHN0eWxldXpAbWFpbC5ydSIsInJvbGUiOiJ1c2VyIiwiYXBpX3Rva2VuIjpudWxsLCJzdGF0dXMiOiJhY3RpdmUiLCJzbXNfYXBpX2xvZ2luIjoiZXNraXoyIiwic21zX2FwaV9wYXNzd29yZCI6ImUkJGsheiIsInV6X3ByaWNlIjo1MCwidWNlbGxfcHJpY2UiOjExNSwidGVzdF91Y2VsbF9wcmljZSI6bnVsbCwiYmFsYW5jZSI6Mjk5ODAwLCJpc192aXAiOjAsImhvc3QiOiJzZXJ2ZXIxIiwiY3JlYXRlZF9hdCI6IjIwMjMtMDQtMTFUMTM6MDU6MjQuMDAwMDAwWiIsInVwZGF0ZWRfYXQiOiIyMDIzLTA1LTEyVDEyOjAxOjA0LjAwMDAwMFoiLCJ3aGl0ZWxpc3QiOm51bGx9LCJpYXQiOjE2ODM4OTY5MjYsImV4cCI6MTY4NjQ4ODkyNn0.HNRKAYsrVIP1mCfr914lyEy2oLN5BFasrOcl4ziHV1M";
    private final AuthRepository repository;
    private final ResourceBundleService resourceBundleService;


    @Autowired
    public AuthService(AuthRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        var optional = repository.findByPhoneUser(phone);
        if (optional.isEmpty()) throw new UsernameNotFoundException("Bad Credentials");
        return new CustomUserDetail(optional.get());
    }


    @Transactional
    public String sendSms(SendSmsDTO dto, Language language) {
        var optional = repository.findByPhoneUser(dto.getPhone());
        if (optional.isPresent()) {
            var entity = optional.get();
//            int attempt = repository.countBySmsCodeHistory(entity.getId());
//            if (attempt >= 5)
//                throw new LimitOverException(resourceBundleService.getMessage("limit.over.sms", language));

            switch (entity.getStatus()) {
                case ACTIVE:
                    return sendSms(entity, dto, ProfileStatus.ACTIVE, language);
                case BLOCK:
                    throw new ProfileBlockedException(resourceBundleService.getMessage("profile.blocked", language.name()));
                case NOT_ACTIVE:
                    return sendSms(entity, dto, ProfileStatus.NOT_ACTIVE, language);
            }
        }

        getProfile(dto);
        return "Tasdiqlash sms habar yuborildi";

    }

    public void getProfile(SendSmsDTO dto) {
        String smsCode = randomSmsCode();
        ProfileEntity profile = new ProfileEntity();
        profile.setPhoneUser(dto.getPhone());
        profile.setSmsTime(LocalDateTime.now());
        profile.setSmsCode(MD5.md5(smsCode));
        profile.setRole(ProfileRole.ROLE_USER);
        profile.setStatus(ProfileStatus.NOT_ACTIVE);
        repository.save(profile);
        sendSmsCode(removePlusSign(dto.getPhone()), smsCode);
    }

    @Transactional
    public String sendSms(ProfileEntity entity, SendSmsDTO dto, ProfileStatus status, Language language) {
        String smsCode = randomSmsCode();
        entity.setSmsCode(MD5.md5(smsCode));
        entity.setSmsTime(LocalDateTime.now());
        entity.setPhoneUser(dto.getPhone());
        entity.setStatus(status);
        repository.save(entity);
        sendSmsCode(removePlusSign(dto.getPhone()), smsCode);
        return "Tasdiqlash sms habar yuborildi";
    }

    public ProfileResponseDTO registration(Long userId, RegistrationDTO dto, Language language) {
        var optional = repository.findById(userId);
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language.name()));
        } else if (optional.get().getStatus().equals(ProfileStatus.ACTIVE))
            throw new ProfileAlReadyRegistrationException(resourceBundleService.getMessage("profile.ready.active", language));
        var savedProfile = repository.save(getProfile(optional.get(), dto));
        return getDTO(savedProfile);
    }


    public ProfileEntity getProfile(ProfileEntity profile, RegistrationDTO dto) {
        profile.setNameUz(dto.getName());
        profile.setNameRu(TranslateUtil.LatinToAcrylic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslateUtil.LatinToAcrylic(dto.getSurname()));
        profile.setBirthdate(dto.getBirthdate());
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslateUtil.LatinToAcrylic(dto.getProfession()));
        profile.setTeam(dto.getTeam());
        profile.setRegion(dto.getRegion());
        profile.setDistrict(dto.getDistrict());
        profile.setPhoneHome(dto.getPhoneHome());
        profile.setStatus(ProfileStatus.ACTIVE);
        profile.setScore(0L);
        return profile;
    }

    public void sendSmsCode(String phone, String message) {
        try {
            var client = new OkHttpClient().newBuilder().build();
            MediaType.parse("text/plain");
            var body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mobile_phone", phone)
                    .addFormDataPart("message", "LegaStore:\nTasdiqlash kodi: " + message)
                    .addFormDataPart("from", "4546")
                    .addFormDataPart("callback_url", "http://0000.uz/test.php")
                    .build();
            var request = new Request.Builder().url("https:notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Authorization", "Bearer " + token)
                    .method("POST", body)
                    .build();
            client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String randomSmsCode() {
        Random random = new Random(System.currentTimeMillis());
        return String.valueOf(((1 + random.nextInt(9)) * 10000 + random.nextInt(10000)));
    }

    public LoginResponseDTO verification(VerificationDTO dto, Language language) {
        var optional = repository.findByPhoneUser(dto.getPhone());
        if (optional.isEmpty()) throw new PhoneNotExistsException(resourceBundleService.
                getMessage("phone.not.exists", language.name()));

        var entity = optional.get();
        if (!entity.getSmsCode().equals(MD5.md5(dto.getPassword()))) {
            throw new PasswordIncorrectException(resourceBundleService.getMessage("password.wrong", language.name()));
        } else if (entity.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new ProfileBlockedException(resourceBundleService.getMessage("profile.blocked", language));
        } else if (LocalDateTime.now().isAfter(entity.getSmsTime().plusMinutes(2))) {
            throw new SmsTimeOverException(resourceBundleService.getMessage("sms.time.over", language));
        }
        var response = new LoginResponseDTO();
        response.setStatus(entity.getStatus());
        response.setRole(entity.getRole());
        response.setToken(JwtUtil.encode(entity.getPhoneUser(), entity.getRole()));
        return response;
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
        dto.setRegionRu(TranslateUtil.LatinToAcrylic(entity.getRegion()));
        dto.setDistrictUz(entity.getDistrict());
        dto.setDistrictRu(TranslateUtil.LatinToAcrylic(entity.getDistrict()));
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());
        return dto;
    }


    public String removePlusSign(String phoneNumber) {
        return phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
    }


}

