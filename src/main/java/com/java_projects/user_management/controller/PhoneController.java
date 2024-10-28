package com.java_projects.user_management.controller;

import com.java_projects.user_management.entities.PhoneEntity;
import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.models.PhoneModel;
import com.java_projects.user_management.responses.ResponseBuilder;
import com.java_projects.user_management.services.PhoneService;
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
@Tag(name = "Phone Controller", description = "Operations related to user phone management")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping({"/{userId}/phones", "/{userId}/phones/"})
    @Operation(summary = "Get All Phones for User", description = "Retrieve all phone numbers associated with the specified user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all phones.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "404", description = "User not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<List<PhoneEntity>> getAllPhones(@PathVariable UUID userId) throws InternalServerException, NotFoundException {
        try {
            return ResponseEntity.ok(phoneService.getAllPhones(userId));
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found for the provided ID.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Failed to retrieve phones due to server error.");
        }
    }

    @GetMapping("/{userId}/phone/{phoneId}")
    @Operation(summary = "Get Phone by ID", description = "Retrieve a specific phone by user ID and phone ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the phone.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "404", description = "Phone not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<PhoneEntity> getPhoneById(@PathVariable UUID userId, @PathVariable UUID phoneId) throws NotFoundException {
        try {
            return ResponseEntity.ok(phoneService.getPhoneById(phoneId, userId));
        } catch (NotFoundException e) {
            throw new NotFoundException("Phone not found for the provided IDs.");
        }
    }

    @PostMapping("/{userId}/phone")
    @Operation(summary = "Create Phone for User", description = "Create a new phone number for a specific user.")
    @ApiResponse(responseCode = "200", description = "Phone created successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "404", description = "User not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to create phone.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> createPhone(@PathVariable UUID userId, @RequestBody PhoneModel phoneModel) throws InternalServerException, NotFoundException {
        try {
            phoneService.createPhone(phoneModel, userId);
            ResponseBuilder response = new ResponseBuilder("Phone created successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found for the provided ID.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while creating the phone.");
        }
    }

    @PutMapping("/{userId}/phone/{phoneId}")
    @Operation(summary = "Update Phone", description = "Update a specific phone by user ID and phone ID.")
    @ApiResponse(responseCode = "200", description = "Phone updated successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "404", description = "User or phone not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to update phone.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> updatePhone(@PathVariable UUID userId, @PathVariable UUID phoneId, @RequestBody PhoneModel phoneModel) throws InternalServerException, NotFoundException {
        try {
            phoneService.updatePhone(phoneModel, phoneId, userId);
            ResponseBuilder response = new ResponseBuilder("Phone updated successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("User or phone not found for the provided IDs.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while updating the phone.");
        }
    }

    @DeleteMapping("/{userId}/phone/{phoneId}")
    @Operation(summary = "Delete Phone", description = "Delete a specific phone by user ID and phone ID.")
    @ApiResponse(responseCode = "200", description = "Phone deleted successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to delete phone.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> deletePhone(@PathVariable UUID userId, @PathVariable UUID phoneId) throws InternalServerException {
        try {
            phoneService.deletePhoneById(phoneId, userId);
            ResponseBuilder response = new ResponseBuilder("Phone deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while deleting the phone.");
        }
    }

    @PostMapping("/{userId}/phone/{phoneId}/request-verification")
    @Operation(summary = "Request Phone Verification", description = "Request a verification for a specific phone.")
    @ApiResponse(responseCode = "200", description = "Verification request sent successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to send verification request.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> requestVerification(@PathVariable UUID userId, @PathVariable UUID phoneId, HttpServletRequest request) throws InternalServerException {
        try {
            String verificationUrl = phoneService.requestVerification(phoneId, userId, request);
            ResponseBuilder response = new ResponseBuilder("Verification request sent. URL: " + verificationUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new InternalServerException("Failed to send verification request.");
        }
    }

    @PostMapping("/{userId}/phone/{phoneId}/verify")
    @Operation(summary = "Verify Phone", description = "Verify the phone using a verification token.")
    @ApiResponse(responseCode = "200", description = "Phone verified successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "500", description = "Failed to verify phone.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> verifyPhone(@PathVariable UUID userId, @PathVariable UUID phoneId, @RequestParam String token) throws NotFoundException, InternalServerException {
        try {
            phoneService.verifyPhone(phoneId, userId, token);
            ResponseBuilder response = new ResponseBuilder("Phone verified successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @PostMapping("/{userId}/phone/{phoneId}/make-primary")
    @Operation(summary = "Make Primary Phone", description = "Set the specified phone as the primary phone for the user.")
    @ApiResponse(responseCode = "200", description = "Phone set as primary successfully.", content = {@Content(schema = @Schema(implementation = PhoneEntity.class))})
    @ApiResponse(responseCode = "404", description = "Phone or user not found.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "500", description = "Failed to set phone as primary.", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> makePrimaryPhone(@PathVariable UUID userId, @PathVariable UUID phoneId) throws NotFoundException, InternalServerException {
        try {
            phoneService.makePhonePrimary(userId, phoneId);
            ResponseBuilder response = new ResponseBuilder("Phone set as primary successfully.");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            throw new NotFoundException("Phone or user not found.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while setting the phone as primary.");
        }
    }
}
