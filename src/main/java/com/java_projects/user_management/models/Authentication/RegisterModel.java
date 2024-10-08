package com.java_projects.user_management.models.Authentication;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.entities.PhoneEntity;
import com.java_projects.user_management.models.EmailModel;
import com.java_projects.user_management.models.PhoneModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterModel {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String image_url;
    private EmailModel email;
    private PhoneModel phone;
}
