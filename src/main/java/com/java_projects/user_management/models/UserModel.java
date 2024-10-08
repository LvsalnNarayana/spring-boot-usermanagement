package com.java_projects.user_management.models;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.entities.PhoneEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String image_url;

}
