package com.example.repository;

import com.example.entity.AdminMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMesageRepository extends JpaRepository<AdminMessageEntity, Long> {

    List<AdminMessageEntity> findAllByOrderByIdDesc();
}
