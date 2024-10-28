package com.java_projects.user_management.models;
import lombok.*;

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
