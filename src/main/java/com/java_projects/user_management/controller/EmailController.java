package com.java_projects.user_management.controller;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.models.EmailModel;
import com.java_projects.user_management.responses.ResponseBuilder;
import com.java_projects.user_management.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Email Controller")
public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping({"/{userId}/email", "/{userId}/email/"})
    public ResponseEntity<List<EmailEntity>> getAllEmails(@PathVariable UUID userId) throws InternalServerException, NotFoundException {
        try {
            return ResponseEntity.ok(emailService.getAllEmails(userId));
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("/{userId}/email/{emailId}")
    public ResponseEntity<EmailEntity> getEmailById(@PathVariable UUID userId, @PathVariable UUID emailId) throws NotFoundException, InternalServerException {
        try {
            return ResponseEntity.ok(emailService.getEmailById(emailId, userId));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PostMapping("/{userId}/email")
    public ResponseEntity<ResponseBuilder> createEmail(@PathVariable UUID userId, @RequestBody EmailModel emailModel) throws InternalServerException, NotFoundException {
        try {
            emailService.createEmail(emailModel, userId);
            ResponseBuilder responseBuilder = new ResponseBuilder("Email created successfully");
            return ResponseEntity.ok(responseBuilder);
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PutMapping("/{userId}/email/{emailId}")
    public ResponseEntity<ResponseBuilder> updateEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @RequestBody EmailModel emailModel) throws InternalServerException, NotFoundException {
        try {
            emailService.updateEmail(emailModel, emailId, userId);
            ResponseBuilder response = new ResponseBuilder("Email updated successfully");
            return ResponseEntity.ok(response);
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/email/{emailId}")
    public ResponseEntity<ResponseBuilder> deleteEmail(@PathVariable UUID userId, @PathVariable UUID emailId) throws InternalServerException {
        try {
            emailService.deleteEmailById(emailId, userId);
            ResponseBuilder responseBuilder = new ResponseBuilder("Email deleted successfully");
            return ResponseEntity.ok(responseBuilder);
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }

    }

    @PostMapping("/{userId}/email/{emailId}/request-verification")
    public ResponseEntity<ResponseBuilder> requestVerification(@PathVariable UUID userId, @PathVariable UUID emailId, HttpServletRequest request) throws InternalServerException {
        try {
            String verificationUrl = emailService.requestVerification(emailId, userId, request);
            ResponseBuilder response = new ResponseBuilder(verificationUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @PostMapping("/{userId}/email/{emailId}/verify")
    public ResponseEntity<ResponseBuilder> verifyEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @RequestParam(required = true) String token) throws NotFoundException {
        try {
            emailService.verifyEmail(emailId, userId, token);
            ResponseBuilder response = new ResponseBuilder("Email verified successfully");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PostMapping("/{userId}/email/{emailId}/make-primary")
    @Operation(description = "Makes the specified email primary for the given user")
    public ResponseEntity<ResponseBuilder> makePrimaryEmail(@PathVariable UUID userId, @PathVariable UUID emailId) throws NotFoundException, InternalServerException {
        try {
            emailService.makeEmailPrimary(userId, emailId);
            ResponseBuilder response = new ResponseBuilder("Email set to primary successfully");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
