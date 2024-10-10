package com.java_projects.user_management.repository;

import com.java_projects.user_management.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u FROM users u WHERE u.username LIKE %?1%")
    public List<UserEntity> findUsersByUsername(String username);

}
