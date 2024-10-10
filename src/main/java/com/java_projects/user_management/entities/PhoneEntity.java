package com.java_projects.user_management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phones")
public class PhoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @PrimaryKeyJoinColumn
    private UUID id;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "country_code", nullable = false,length = 10)
    private String countryCode;
    @Column(name = "verified", nullable = false)
    private Boolean verified;
    @Column(name = "primary_phone", nullable = false)
    private Boolean primaryPhone;
    @Column(name = "created_at", nullable = false)
    private Date created_at;
    @Column(name = "updated_at",nullable = false)
    private Date updated_at;
    @ManyToOne
    @JsonIgnore
    private UserEntity user;
}
