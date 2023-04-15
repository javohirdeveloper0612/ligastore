package com.example.repository;

import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<ProfileEntity,Long> {
    Optional<ProfileEntity> findByUsernameAndPassword(String username,String password);

    Optional<ProfileEntity> findByUsername(String username);

    Optional<ProfileEntity> findByPhoneUser(String phone);

    boolean existsByUsername(String username);
}
