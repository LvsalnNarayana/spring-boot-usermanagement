package com.java_projects.user_management.controller;

import com.java_projects.user_management.entities.UserEntity;
import com.java_projects.user_management.exceptions.*;
import com.java_projects.user_management.models.UserModel;
import com.java_projects.user_management.responses.ResponseBuilder;
import com.java_projects.user_management.services.UserService;
import com.java_projects.user_management.utils.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "User Controller", description = "User Management Operations")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get User Details by ID", description = "Fetches user details based on the provided UUID.")
    @ApiResponse(responseCode = "200", description = "User found", content = {@Content(schema = @Schema(implementation = UserEntity.class))})
    @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID userId) throws BadRequestException, NotFoundException, InternalServerException {
        if (userId == null) {
            throw new BadRequestException("ID cannot be null.");
        }
        try {
            UUID.fromString(userId.toString());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format.");
        }
        try {
            UserEntity user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found.");
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error.");
        }
    }

    @PostMapping("/users")
    @Operation(summary = "Create User", description = "Creates a new user with the provided details.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for creating a new user.",
            content = {@Content(schema = @Schema(implementation = UserModel.class))}
    )
    @ApiResponse(responseCode = "201", description = "User created successfully", content = {@Content(schema = @Schema(implementation = ResponseBuilder.class))})
    @ApiResponse(responseCode = "500", description = "Error occurred during user creation", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> createUser(@RequestBody UserModel userDetails) throws UserNotCreatedException {
        try {
            userService.createUser(userDetails);
            ResponseBuilder response = new ResponseBuilder("User created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotCreatedException e) {
            throw new UserNotCreatedException("Failed to create user: " + e.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    @Operation(summary = "Update User", description = "Updates the user details for the specified UUID.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for updating an existing user.",
            content = {@Content(schema = @Schema(implementation = UserModel.class))}
    )
    @ApiResponse(responseCode = "200", description = "User updated successfully", content = {@Content(schema = @Schema(implementation = ResponseBuilder.class))})
    @ApiResponse(responseCode = "500", description = "Error occurred during user update", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> updateUser(@PathVariable UUID userId, @RequestBody UserModel userDetails) throws UserUpdateFailedException, NotFoundException {
        try {
            userService.updateUser(userId, userDetails);
            ResponseBuilder response = new ResponseBuilder("User updated successfully.");
            return ResponseEntity.ok(response);
        } catch (UserUpdateFailedException e) {
            throw new UserUpdateFailedException("Failed to update user: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found: " + e.getMessage());
        }
    }

    @GetMapping({"/users", "/users/"})
    @Operation(summary = "Get Users", description = "Fetches all users or users filtered by username.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = {@Content(schema = @Schema(implementation = UserEntity.class))})
    @ApiResponse(responseCode = "500", description = "Error occurred while fetching users", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<List<UserEntity>> getAllUsers(@RequestParam(required = false) String username) throws InternalServerException {
        try {
            List<UserEntity> users;
            if (username != null) {
                users = userService.getUsersByUsername(username);
            } else {
                users = userService.getAllUsers();
            }
            return ResponseEntity.ok(users);
        } catch (InternalServerException e) {
            throw new InternalServerException("Error occurred while retrieving users: " + e.getMessage());
        }
    }
}
