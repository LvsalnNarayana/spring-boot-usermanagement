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
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UserRepository userRepository;

    public List<EmailEntity> getAllEmails(UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        try {
            return emailRepository.findByUser(user);
        } catch (Exception e) {
            throw new InternalServerException("Failed to get emails by user");
        }
    }

    public EmailEntity getEmailById(UUID emailId, UUID userId) throws NotFoundException {
        try {
            return emailRepository.findByIdAndUser_Id(emailId, userId).orElseThrow(() -> new NotFoundException("Email not found"));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public void createEmail(EmailModel email, UUID userId) throws NotFoundException, InternalServerException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmail(email.getEmail());
        emailEntity.setCreated_at(new Date());
        emailEntity.setUpdated_at(new Date());
        emailEntity.setUser(user);
        try {
            emailRepository.save(emailEntity);
        } catch (Exception e) {
            throw new InternalServerException("Error creating email");
        }
    }

    public void updateEmail(EmailModel email, UUID emailId, UUID userId) throws NotFoundException, InternalServerException {
        EmailEntity existingEmail = emailRepository.findByIdAndUser_Id(emailId, userId).orElseThrow(() -> new NotFoundException("Email not found"));
        existingEmail.setEmail(email.getEmail());
        existingEmail.setUpdated_at(new Date());
        try {
            emailRepository.save(existingEmail);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void deleteEmailById(UUID emailId, UUID userId) throws InternalServerException {
        try {
            emailRepository.deleteByIdAndUser_Id(emailId, userId);
        } catch (Exception e) {
            throw new InternalServerException("Failed to delete mail");
        }
    }

    public String requestVerification(UUID emailId, UUID userId, HttpServletRequest request) {
        String verificationToken = UUID.randomUUID().toString();
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        System.out.println(appUrl);
        return appUrl + "/verify?token=" + verificationToken;
    }

    public void verifyEmail(UUID emailId, UUID userId, String token) throws NotFoundException {
        try {
            EmailEntity email = emailRepository.findByIdAndUser_Id(emailId, userId).orElseThrow(() -> new NotFoundException("Email not found"));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public void makeEmailPrimary(UUID userId, UUID emailId) throws NotFoundException, InternalServerException {
        List<EmailEntity> userEmails = emailRepository.findAllByUser_Id(userId).orElseThrow(() -> new NotFoundException("User not found"));

        try {
            for (EmailEntity userEmail : userEmails) {
                userEmail.setPrimaryEmail(userEmail.getId().equals(emailId));
            }
            emailRepository.saveAll(userEmails);
        } catch (Exception e) {
            throw new InternalServerException("Failed to set primary email id");
        }
    }


}
