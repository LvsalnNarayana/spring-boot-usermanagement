package com.java_projects.user_management.entities;

import com.java_projects.user_management.utils.enums.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, table = "users")
    private UUID id;
    @Column(name = "role", nullable = false)
    private UserRoles role = UserRoles.USER;
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "firstname", unique = false, nullable = false, length = 50)
    private String firstname;
    @Column(name = "lastname", unique = false, nullable = true, length = 50)
    private String lastname;
    @Column(name = "locked", nullable = false)
    private boolean locked = false;
    @Column(name = "has_image", nullable = false)
    private boolean has_image = false;
    @Column(name = "image_url", nullable = true, length = 50)
    private String image_url;
    @Column(name = "primary_email_id", unique = false, nullable = true, length = 50)
    private UUID primary_email_id;
    @Column(name = "primary_phone_id", unique = false, nullable = true, length = 50)
    private UUID primary_phone_id;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created_at;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
    @Column(name = "delete_self_enabled", nullable = false)
    private boolean delete_self_enabled = false;
    @Column(name = "otp_enabled", nullable = false)
    private boolean otp_enabled = false;
    @Column(name = "last_active_at")
    private LocalDateTime last_active_at;
    @Column(name = "create_organization_enabled", nullable = false)
    private boolean create_organization_enabled = false;
    @Column(name = "emails")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,targetEntity = EmailEntity.class)
    private List<EmailEntity> emails;
    @Column(name = "phones")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, targetEntity = PhoneEntity.class)
    private List<PhoneEntity> phones;
}

