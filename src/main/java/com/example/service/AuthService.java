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
import com.example.util.TranslaterUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    private final static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM4MjcsInJvbGUiOm51bGwsImRhdGEiOnsiaWQiOjM4MjcsIm5hbWUiOiJPc29iZW5ubyIsImVtYWlsIjoidW1pZHN0eWxldXpAbWFpbC5ydSIsInJvbGUiOm51bGwsImFwaV90b2tlbiI6bnVsbCwic3RhdHVzIjoiYWN0aXZlIiwic21zX2FwaV9sb2dpbiI6ImVza2l6MiIsInNtc19hcGlfcGFzc3dvcmQiOiJlJCRrIXoiLCJ1el9wcmljZSI6NTAsInVjZWxsX3ByaWNlIjoxMTUsInRlc3RfdWNlbGxfcHJpY2UiOm51bGwsImJhbGFuY2UiOjQ4NTAsImlzX3ZpcCI6MCwiaG9zdCI6InNlcnZlcjEiLCJjcmVhdGVkX2F0IjoiMjAyMy0wNC0xMVQxMzowNToyNC4wMDAwMDBaIiwidXBkYXRlZF9hdCI6IjIwMjMtMDQtMTJUMTA6MjI6MTUuMDAwMDAwWiIsIndoaXRlbGlzdCI6bnVsbH0sImlhdCI6MTY4MTI5NTEwMCwiZXhwIjoxNjgzODg3MTAwfQ.GGj4-SMvy6csvI3cQOPHqbikXhmcwzUXGsrcl6n92XM";

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
            int attempt = repository.countBySmsCodeHistory(entity.getId());
            if (attempt >= 5) throw new LimitOverException(resourceBundleService.getMessage("limit.over.sms", language));

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
        profile.setNameRu(TranslaterUtil.latinToCryllic(dto.getName()));
        profile.setSurnameUz(dto.getSurname());
        profile.setSurnameRu(TranslaterUtil.latinToCryllic(dto.getSurname()));
        profile.setBirthdate(dto.getBirthdate());
        profile.setProfessionUz(dto.getProfession());
        profile.setProfessionRu(TranslaterUtil.latinToCryllic(dto.getProfession()));
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
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mobile_phone", phone)
                    .addFormDataPart("message", "LigaStore:\nTasdiqlash kodi: " + message)
                    .addFormDataPart("from", "4546")
                    .addFormDataPart("callback_url", "http://0000.uz/test.php")
                    .build();
            Request request = new Request.Builder().url("https:notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Authorization", "Bearer " + token)
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String randomSmsCode() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf(((1 + r.nextInt(9)) * 10000 + r.nextInt(10000)));
    }

    public LoginResponseDTO verification(VerificationDTO dto, Language language) {
        var optional = repository.findByPhoneUser(dto.getPhone());
        if (optional.isEmpty()) throw new PhoneNotExistsException(resourceBundleService.
                getMessage("phone.not.exists", language.name()));

        ProfileEntity entity = optional.get();
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
        dto.setRegionRu(TranslaterUtil.latinToCryllic(entity.getRegion()));
        dto.setDistrictUz(entity.getDistrict());
        dto.setDistrictRu(TranslaterUtil.latinToCryllic(entity.getDistrict()));
        dto.setPhoneUser(entity.getPhoneUser());
        dto.setPhoneHome(entity.getPhoneHome());
        dto.setScore(entity.getScore());
        return dto;
    }


    public String removePlusSign(String phoneNumber) {
        return phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
    }


}

