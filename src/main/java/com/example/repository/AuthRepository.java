package com.example.repository;

import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUsernameAndPassword(String username, String password);

    Optional<ProfileEntity> findByUsername(String username);

    Optional<ProfileEntity> findByPhoneUser(String phone);

    boolean existsByUsername(String username);
@Modifying
@Query(value = "update profile set password = ?1 where id = ?2",nativeQuery = true)
void updatePasswordById(String password,Long id);

}
