package com.java_projects.user_management.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneModel {
    private String phone;
    private String countryCode;
    private Boolean verified;
    private Boolean primaryPhone;
}
