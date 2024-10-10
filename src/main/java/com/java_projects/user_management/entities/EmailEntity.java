package com.java_projects.user_management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity(name = "emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @PrimaryKeyJoinColumn
    private UUID id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;
    @Column(name = "primary_email", nullable = false)
    private Boolean primaryEmail = false;
    @Column(name = "created_at", nullable = false)
    private Date created_at;
    @Column(name = "updated_at",nullable = false)
    private Date updated_at;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;
}
