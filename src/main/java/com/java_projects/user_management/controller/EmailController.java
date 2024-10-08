package com.java_projects.user_management.controller;

import com.java_projects.user_management.models.EmailModel;
import com.java_projects.user_management.responses.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Email Controller")
public class EmailController {
    @GetMapping({"/{userId}/email","/{userId}/email/"})
    public ResponseEntity<ResponseBuilder> getAllEmails(@PathVariable UUID userId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("All Emails of user retrieved successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @GetMapping("/{userId}/email/{emailId}")
    public ResponseEntity<ResponseBuilder> getAllEmailById(@PathVariable UUID userId, @PathVariable UUID emailId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email with id retrieved successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PostMapping("/{userId}/email")
    public ResponseEntity<ResponseBuilder> createEmail(@PathVariable UUID userId, @RequestBody EmailModel emailModel) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email created successfully");
        return ResponseEntity.ok(responseBuilder);
    }
    @PutMapping("/{userId}/email/{emailId}")
    public ResponseEntity<ResponseBuilder> updateEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @RequestBody EmailModel emailModel) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email updated successfully");
        return ResponseEntity.ok(responseBuilder);
    }
    @DeleteMapping("/{userId}/email/{emailId}")
    public ResponseEntity<ResponseBuilder> deleteEmail(@PathVariable UUID userId, @PathVariable UUID emailId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email deleted successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @GetMapping("/{userId}/email/{emailId}/request-verification")
    public ResponseEntity<ResponseBuilder> getRequestVerification(@PathVariable UUID userId, @PathVariable UUID emailId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email request verification successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PutMapping("/{userId}/email/{emailId}/verify")
    public ResponseEntity<ResponseBuilder> verifyEmail(@PathVariable UUID userId, @PathVariable UUID emailId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email verified successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PutMapping("/{userId}/email/{emailId}/make-primary")
    @Operation(description = "Makes the specified email primary for the given user")
    public ResponseEntity<ResponseBuilder> makePrimaryEmail(@PathVariable(required = false) UUID userId, @PathVariable(required = false) UUID emailId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        ResponseBuilder responseBuilder = new ResponseBuilder("Email made primary successfully");
        return new ResponseEntity<>(responseBuilder,headers, HttpStatus.OK);
    }
}
