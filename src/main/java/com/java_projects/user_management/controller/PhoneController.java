package com.java_projects.user_management.controller;

import com.java_projects.user_management.models.PhoneModel;
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
@Tag(name = "Phone Controller")
public class PhoneController {
    @GetMapping({"/{userId}/phones","/{userId}/phones/"})
    public ResponseEntity<ResponseBuilder> getAllPhones(@PathVariable UUID userId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("All Phones of user retrieved successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @GetMapping("/{userId}/phone/{phoneId}")
    public ResponseEntity<ResponseBuilder> getPhoneById(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone with id retrieved successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PostMapping("/{userId}/phone")
    public ResponseEntity<ResponseBuilder> createPhone(@PathVariable UUID userId, @RequestBody PhoneModel phoneModel) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Email created successfully");
        return ResponseEntity.ok(responseBuilder);
    }
    @PutMapping("/{userId}/phone/{phoneId}")
    public ResponseEntity<ResponseBuilder> updatePhone(@PathVariable UUID userId, @PathVariable UUID phoneId, @RequestBody PhoneModel phoneModel) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone updated successfully");
        return ResponseEntity.ok(responseBuilder);
    }
    @DeleteMapping("/{userId}/phone/{phoneId}")
    public ResponseEntity<ResponseBuilder> deletePhone(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone  deleted successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @GetMapping("/{userId}/phone/{phoneId}/request-verification")
    public ResponseEntity<ResponseBuilder> getRequestVerification(@PathVariable UUID userId, @PathVariable UUID phoneid) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone request verification successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PutMapping("/{userId}/phone/{phoneId}/verify")
    public ResponseEntity<ResponseBuilder> verifyEmail(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone verified successfully");
        return ResponseEntity.ok(responseBuilder);
    }

    @PutMapping("/{userId}/phone/{phoneId}/make-primary")
    @Operation(description = "Makes the specified email primary for the given user")
    public ResponseEntity<ResponseBuilder> makePrimaryEmail(@PathVariable(required = false) UUID userId, @PathVariable(required = false) UUID phoneId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        ResponseBuilder responseBuilder = new ResponseBuilder("Phone made primary successfully");
        return new ResponseEntity<>(responseBuilder,headers, HttpStatus.OK);
    }
}
