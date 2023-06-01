package com.example.component;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProfileRepository profileRepository;

    @Value(value = "${spring.sql.init.mode}")
    private String modeType;

    @Autowired
    public DataLoader(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        ProfileEntity profile = new ProfileEntity();

        if (modeType.equals("always")) {
            profile.setNameUz("ADMIN");
            profile.setNameRu("ADMIN");
            profile.setRole(ProfileRole.ROLE_ADMIN);
            profile.setStatus(ProfileStatus.ACTIVE);
            profile.setPhoneUser("+998996731741");
            profile.setPhoneHome("+998911234567");
            profile.setScore(0D);
            profile.setSmsCode("12345");
            profileRepository.save(profile);
        }
    }
}
