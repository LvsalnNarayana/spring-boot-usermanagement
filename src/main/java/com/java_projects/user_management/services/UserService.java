package com.java_projects.user_management.services;

import com.java_projects.user_management.exceptions.InternalServerException;
import com.java_projects.user_management.exceptions.NotFoundException;
import com.java_projects.user_management.exceptions.UserNotCreatedException;
import com.java_projects.user_management.exceptions.UserUpdateFailedException;
import org.springframework.stereotype.Service;
import com.java_projects.user_management.entities.UserEntity;
import com.java_projects.user_management.models.UserModel;
import com.java_projects.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserEntity getUserById(UUID user_id) throws NotFoundException, InternalServerException {
        Optional<UserEntity> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            return user.get();
        }
        if (!userRepository.existsById(user_id)) {
            throw new NotFoundException("User not found");
        }
        throw new InternalServerException("Something went wrong");

    }

    public List<UserEntity> getAllUsers() throws InternalServerException {
        return userRepository.findAll();
    }

    public void createUser(UserModel userModel) throws UserNotCreatedException {
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userModel.getUsername());
            userEntity.setFirstname(userModel.getFirstname());
            userEntity.setLastname(userModel.getLastname());
            userEntity.setPassword(userModel.getPassword());
            userEntity.setImage_url(userModel.getImage_url());
            userEntity.setHas_image(!(userModel.getImage_url() == null));
            userEntity.setCreated_at(LocalDateTime.parse(LocalDateTime.now().toString()));
            userEntity.setUpdated_at(LocalDateTime.parse(LocalDateTime.now().toString()));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserNotCreatedException("Error creating user");
        }
    }

    public void updateUser(UUID userId, UserModel userModel) throws UserUpdateFailedException, NotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (userModel.getUsername() != null && !userModel.getUsername().isEmpty()) {
            userEntity.setUsername(userModel.getUsername());
        }
        if (userModel.getFirstname() != null && !userModel.getFirstname().isEmpty()) {
            userEntity.setFirstname(userModel.getFirstname());
        }
        if (userModel.getLastname() != null && !userModel.getLastname().isEmpty()) {
            userEntity.setLastname(userModel.getLastname());
        }
        if (userModel.getImage_url() != null && !userModel.getImage_url().isEmpty()) {
            userEntity.setImage_url(userModel.getImage_url());
        }
        userEntity.setHas_image(!(userModel.getImage_url() == null));
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserUpdateFailedException("Failed to update user");
        }
    }

    public void deleteUser(UUID userId) throws InternalServerException {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong");
        }
    }

    public List<UserEntity> getUsersByUsername(String username) throws InternalServerException {
        try {
            return userRepository.findUsersByUsername(username);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong");
        }
    }
}
