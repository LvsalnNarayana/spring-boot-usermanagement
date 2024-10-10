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
@Tag(name = "User Controller")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    @Operation(method = "GET", description = "Get user details By ID")
    @ApiResponse(responseCode = "200", description = "Response If User is found", content = {@Content(schema = @Schema(implementation = UserEntity.class))})
    @ApiResponse(responseCode = "404", description = "Response If User is not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    @ApiResponse(responseCode = "400", description = "Response If UUID is null or not valid format", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID userId) throws InternalServerException, BadRequestException, NotFoundException {
        if (userId == null) {
            throw new BadRequestException("ID cannot be null.");
        }
        try {
            UUID.fromString(userId.toString());
        } catch (Exception e) {
            throw new BadRequestException("Invalid UUID format");
        }
        try {
            UserEntity user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }

    @PostMapping({"/users"})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "This is user model body for creating user",
            content = {
                    @Content(
                            schema = @Schema(implementation = UserModel.class)
                    )
            }
    )
    @ApiResponse(responseCode = "200", description = "This is response for creating user successfully", content = {@Content(schema = @Schema(implementation = ResponseBuilder.class))})
    @ApiResponse(responseCode = "500", description = "This is response for failed user creation", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> createUser(@RequestBody UserModel userDetails) throws UserNotCreatedException {
        try {
            userService.createUser(userDetails);
            ResponseBuilder response = new ResponseBuilder("User created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotCreatedException e) {
            throw new UserNotCreatedException(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "This is request body for updating user",
            content = {
                    @Content(
                            schema = @Schema(implementation = UserModel.class)
                    )
            }
    )
    @ApiResponse(responseCode = "200", description = "This is response of updating users", content = {@Content(schema = @Schema(implementation = ResponseBuilder.class))})
    @ApiResponse(responseCode = "500", description = "This is response if updating user fails", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ResponseBuilder> updateUser(@PathVariable UUID userId, @RequestBody UserModel userDetails) throws UserUpdateFailedException, NotFoundException {
        try {
            userService.updateUser(userId, userDetails);
            ResponseBuilder response = new ResponseBuilder("User updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserUpdateFailedException e) {
            throw new UserUpdateFailedException(e.getMessage());
        } catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping({"/users", "/users/"})
    @ApiResponse(responseCode = "200", description = "This is response for getting all users or getting users by username", content = {@Content(schema = @Schema(implementation = UserEntity.class))})
    @ApiResponse(responseCode = "500", description = "This is response for any error while getting all users or getting users by username", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<List<UserEntity>> getAllUsers(@RequestParam(required = false) String username) throws InternalServerException {
        List<UserEntity> users;
        try {
            if (username != null) {
                users = userService.getUsersByUsername(username);
            } else {
                users = userService.getAllUsers();
            }
        } catch (InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }

        return ResponseEntity.ok(users);
    }

}
