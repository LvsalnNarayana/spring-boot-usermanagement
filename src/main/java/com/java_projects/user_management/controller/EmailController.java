package com.java_projects.user_management.controller;

import com.java_projects.user_management.entities.EmailEntity;
import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.models.EmailModel;
import com.java_projects.user_management.responses.ResponseBuilder;
import com.java_projects.user_management.services.EmailService;
import com.java_projects.user_management.utils.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Email Controller", description = "Operations related to user email management")
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping({"/{userId}/email", "/{userId}/email/"})
    @Operation(summary = "Get All Emails for User", description = "Retrieve all email addresses associated with the specified user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all emails.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "404", description = "User not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<List<EmailEntity>> getAllEmails(@PathVariable UUID userId) throws InternalServerException, NotFoundException {
        try {
            return ResponseEntity.ok(emailService.getAllEmails(userId));
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found for the provided ID.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Failed to retrieve emails due to server error.");
        }
    }

    @GetMapping("/{userId}/email/{emailId}")
    @Operation(summary = "Get Email by ID", description = "Retrieve a specific email by user ID and email ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the email.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "404", description = "Email not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<EmailEntity> getEmailById(@PathVariable UUID userId, @PathVariable UUID emailId) throws NotFoundException {
        try {
            return ResponseEntity.ok(emailService.getEmailById(emailId, userId));
        } catch (NotFoundException e) {
            throw new NotFoundException("Email not found for the provided IDs.");
        }
    }

    @PostMapping("/{userId}/email")
    @Operation(summary = "Create Email for User", description = "Create a new email for a specific user.")
    @ApiResponse(responseCode = "200", description = "Email created successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "404", description = "User not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to create email.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> createEmail(@PathVariable UUID userId, @RequestBody EmailModel emailModel) throws InternalServerException, NotFoundException {
        try {
            emailService.createEmail(emailModel, userId);
            ResponseBuilder response = new ResponseBuilder("Email created successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found for the provided ID.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while creating the email.");
        }
    }

    @PutMapping("/{userId}/email/{emailId}")
    @Operation(summary = "Update Email", description = "Update a specific email by user ID and email ID.")
    @ApiResponse(responseCode = "200", description = "Email updated successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "404", description = "User or email not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to update email.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> updateEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @RequestBody EmailModel emailModel) throws InternalServerException, NotFoundException {
        try {
            emailService.updateEmail(emailModel, emailId, userId);
            ResponseBuilder response = new ResponseBuilder("Email updated successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("User or email not found for the provided IDs.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while updating the email.");
        }
    }

    @DeleteMapping("/{userId}/email/{emailId}")
    @Operation(summary = "Delete Email", description = "Delete a specific email by user ID and email ID.")
    @ApiResponse(responseCode = "200", description = "Email deleted successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to delete email.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> deleteEmail(@PathVariable UUID userId, @PathVariable UUID emailId) throws InternalServerException {
        try {
            emailService.deleteEmailById(emailId, userId);
            ResponseBuilder response = new ResponseBuilder("Email deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while deleting the email.");
        }
    }

    @PostMapping("/{userId}/email/{emailId}/request-verification")
    @Operation(summary = "Request Email Verification", description = "Request a verification email to be sent for a specific email.")
    @ApiResponse(responseCode = "200", description = "Verification email sent successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to send verification email.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> requestVerification(@PathVariable UUID userId, @PathVariable UUID emailId, HttpServletRequest request) throws InternalServerException {
        try {
            String verificationUrl = emailService.requestVerification(emailId, userId, request);
            ResponseBuilder response = new ResponseBuilder("Verification email sent. URL: " + verificationUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new InternalServerException("Failed to send verification email.");
        }
    }

    @PostMapping("/{userId}/email/{emailId}/verify")
    @Operation(summary = "Verify Email", description = "Verify the email using a verification token.")
    @ApiResponse(responseCode = "200", description = "Email verified successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to verify email.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> verifyEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @RequestParam String token) throws NotFoundException, InternalServerException {
        try {
            emailService.verifyEmail(emailId, userId, token);
            ResponseBuilder response = new ResponseBuilder("Email verified successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @PostMapping("/{userId}/email/{emailId}/make-primary")
    @Operation(summary = "Make Primary Email", description = "Set the specified email as the primary email for the user.")
    @ApiResponse(responseCode = "200", description = "Email set as primary successfully.", content = {@Content(schema = @Schema(implementation = EmailEntity.class))})
    @ApiResponse(responseCode = "404", description = "Email or user not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to set email as primary.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> makePrimaryEmail(@PathVariable UUID userId, @PathVariable UUID emailId) throws NotFoundException, InternalServerException {
        try {
            emailService.makeEmailPrimary(userId, emailId);
            ResponseBuilder response = new ResponseBuilder("Email set to primary successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("Email not found for the provided IDs.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while setting the email as primary.");
        }
    }
}
