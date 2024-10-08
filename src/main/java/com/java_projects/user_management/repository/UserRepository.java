package com.java_projects.user_management.repository;

import com.java_projects.user_management.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public List<UserEntity> findByUsername(String username);
}
