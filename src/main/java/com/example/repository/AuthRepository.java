package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByPhoneUser(String phone);
    List<ProfileEntity> findAllByRole(ProfileRole role);


    @Query(value = "select count(*) from sms_code_history where profile_id = ?1",nativeQuery = true)
    int countBySmsCodeHistory(Long id);


}
