package com.java_projects.user_management.services;

import com.java_projects.user_management.entities.PhoneEntity;
import com.java_projects.user_management.entities.UserEntity;
import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.models.PhoneModel;
import com.java_projects.user_management.repository.PhoneRepository;
import com.java_projects.user_management.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    // Retrieve all phone numbers for a user by userId
    public List<PhoneEntity> getAllPhones(UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            return phoneRepository.findByUser(user);
        } catch (Exception e) {
            throw new InternalServerException("Failed to retrieve phone numbers for the user.");
        }
    }

    // Retrieve a specific phone by phoneId and userId
    public PhoneEntity getPhoneById(UUID phoneId, UUID userId) throws NotFoundException {
        return phoneRepository.findByIdAndUser_Id(phoneId, userId)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));
    }

    // Create a new phone number for the user
    public void createPhone(PhoneModel phoneModel, UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setPhone(phoneModel.getPhone());
        phoneEntity.setCountryCode(phoneModel.getCountryCode());
        phoneEntity.setCreated_at(new Date());
        phoneEntity.setUpdated_at(new Date());
        phoneEntity.setVerified(false); // By default, the phone isn't verified
        phoneEntity.setPrimaryPhone(false); // By default, it's not the primary phone
        phoneEntity.setUser(user);

        try {
            phoneRepository.save(phoneEntity);
        } catch (Exception e) {
            throw new InternalServerException("Error occurred while creating the phone number.");
        }
    }

    // Update an existing phone number for a user
    public void updatePhone(PhoneModel phoneModel, UUID phoneId, UUID userId) throws NotFoundException, InternalServerException {
        PhoneEntity existingPhone = phoneRepository.findByIdAndUser_Id(phoneId, userId)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        existingPhone.setPhone(phoneModel.getPhone());
        existingPhone.setCountryCode(phoneModel.getCountryCode());
        existingPhone.setUpdated_at(new Date());

        try {
            phoneRepository.save(existingPhone);
        } catch (Exception e) {
            throw new InternalServerException("Error occurred while updating the phone number.");
        }
    }

    // Delete a phone number by phoneId and userId
    public void deletePhoneById(UUID phoneId, UUID userId) throws InternalServerException {
        try {
            phoneRepository.deleteByIdAndUser_Id(phoneId, userId);
        } catch (Exception e) {
            throw new InternalServerException("Failed to delete the phone number.");
        }
    }

    // Request a verification token for a specific phone number
    public String requestVerification(UUID phoneId, UUID userId, HttpServletRequest request) throws NotFoundException {
        phoneRepository.findByIdAndUser_Id(phoneId, userId)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        String verificationToken = UUID.randomUUID().toString();
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return appUrl + "/verify?token=" + verificationToken;
    }

    // Verify a phone number using a token
    public void verifyPhone(UUID phoneId, UUID userId, String token) throws NotFoundException, InternalServerException {
        PhoneEntity phone = phoneRepository.findByIdAndUser_Id(phoneId, userId)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        phone.setVerified(true);
        phone.setUpdated_at(new Date());

        try {
            phoneRepository.save(phone);
        } catch (Exception e) {
            throw new InternalServerException("Failed to verify the phone number.");
        }
    }

    // Set a phone number as the primary phone for a user
    public void makePhonePrimary(UUID userId, UUID phoneId) throws NotFoundException, InternalServerException {
        List<PhoneEntity> userPhones = phoneRepository.findAllByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            for (PhoneEntity userPhone : userPhones) {
                userPhone.setPrimaryPhone(userPhone.getId().equals(phoneId));
            }
            phoneRepository.saveAll(userPhones);
        } catch (Exception e) {
            throw new InternalServerException("Failed to set the primary phone number.");
        }
    }
}
