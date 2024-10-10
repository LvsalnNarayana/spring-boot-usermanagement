package com.java_projects.user_management.repository;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {
    public List<EmailEntity> findByUser(UserEntity user);

    public Optional<EmailEntity> findByIdAndUser_Id(UUID id, UUID user_id);

    public void deleteByIdAndUser_Id(UUID id, UUID user_id);

    public Optional<List<EmailEntity>> findAllByUser_Id(UUID user_id);
}
