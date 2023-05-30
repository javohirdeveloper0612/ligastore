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


    @Query(value = "SELECT COUNT(DISTINCT id) FROM sms_code_history WHERE profile_id = ?1 AND changed_on >= NOW() - INTERVAL '1 DAY'",nativeQuery = true)
    int countBySmsCodeHistory(Long id);



}
