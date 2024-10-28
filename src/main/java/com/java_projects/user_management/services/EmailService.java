package com.java_projects.user_management.services;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.entities.UserEntity;
import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.models.EmailModel;
import com.java_projects.user_management.repository.EmailRepository;
import com.java_projects.user_management.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EmailEntity> getAllEmails(UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            return emailRepository.findByUser(user);
        } catch (Exception e) {
            throw new InternalServerException("Failed to retrieve emails for the user.");
        }
    }

    public EmailEntity getEmailById(UUID emailId, UUID userId) throws NotFoundException {
        return emailRepository.findByIdAndUser_Id(emailId, userId)
                .orElseThrow(() -> new NotFoundException("Email not found"));
    }

    public void createEmail(EmailModel emailModel, UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmail(emailModel.getEmail());
        emailEntity.setCreated_at(new Date());
        emailEntity.setUpdated_at(new Date());
        emailEntity.setUser(user);

        try {
            emailRepository.save(emailEntity);
        } catch (Exception e) {
            throw new InternalServerException("Error occurred while creating the email.");
        }
    }

    public void updateEmail(EmailModel emailModel, UUID emailId, UUID userId) throws NotFoundException, InternalServerException {
        EmailEntity existingEmail = emailRepository.findByIdAndUser_Id(emailId, userId)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        existingEmail.setEmail(emailModel.getEmail());
        existingEmail.setUpdated_at(new Date());

        try {
            emailRepository.save(existingEmail);
        } catch (Exception e) {
            throw new InternalServerException("Error occurred while updating the email.");
        }
    }

    public void deleteEmailById(UUID emailId, UUID userId) throws InternalServerException {
        try {
            emailRepository.deleteByIdAndUser_Id(emailId, userId);
        } catch (Exception e) {
            throw new InternalServerException("Failed to delete the email.");
        }
    }

    public String requestVerification(UUID emailId, UUID userId, HttpServletRequest request) throws NotFoundException {
        emailRepository.findByIdAndUser_Id(emailId, userId)
                .orElseThrow(() -> new NotFoundException("Email not found"));
        String verificationToken = UUID.randomUUID().toString();
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return appUrl + "/verify?token=" + verificationToken;
    }

    public void verifyEmail(UUID emailId, UUID userId, String token) throws NotFoundException, InternalServerException {
        EmailEntity email = emailRepository.findByIdAndUser_Id(emailId, userId)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        email.setVerified(true);
        email.setUpdated_at(new Date());
        try {
            emailRepository.save(email);
        } catch (Exception e) {
            throw new InternalServerException("Failed to verify email");
        }
    }

    public void makeEmailPrimary(UUID userId, UUID emailId) throws NotFoundException, InternalServerException {
        List<EmailEntity> userEmails = emailRepository.findAllByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            for (EmailEntity userEmail : userEmails) {
                userEmail.setPrimaryEmail(userEmail.getId().equals(emailId));
            }
            emailRepository.saveAll(userEmails);
        } catch (Exception e) {
            throw new InternalServerException("Failed to set the primary email.");
        }
    }
}
