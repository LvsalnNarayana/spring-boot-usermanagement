package com.java_projects.user_management.repository;

import com.java_projects.user_management.entities.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, UUID> {
}
