package com.java_projects.user_management.controller;


import com.java_projects.user_management.models.Authentication.*;
import com.java_projects.user_management.responses.ResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {


    @PostMapping("/login")
    public ResponseEntity<ResponseBuilder> login(@RequestBody LoginModel loginModel) {
        ResponseBuilder response = new ResponseBuilder("Login Successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseBuilder> register(@RequestBody RegisterModel registerModel) {
        ResponseBuilder response = new ResponseBuilder("Registration Successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ResponseBuilder> verifyAccount(@RequestBody AccountVerificationModel accountVerificationModel) {
        ResponseBuilder response = new ResponseBuilder("Account Verified Successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseBuilder> forgotPassword(@RequestBody ForgotPasswordModel forgotPasswordModel) {
        ResponseBuilder response = new ResponseBuilder("Forgot Password Success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ResponseBuilder> getSessionById(@PathVariable UUID sessionId) {
        ResponseBuilder response = new ResponseBuilder("session retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<ResponseBuilder> deleteSessionById(@PathVariable UUID sessionId) {
        ResponseBuilder response = new ResponseBuilder(String.format("session with id deleted successfully", sessionId));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sessions")
    public ResponseEntity<ResponseBuilder> deleteAllSessions() {
        ResponseBuilder response = new ResponseBuilder("sessions deleted successfully");
        return ResponseEntity.ok(response);
    }

}
