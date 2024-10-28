package com.java_projects.user_management.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneModel {
    private String phone;
    private String countryCode;
}
