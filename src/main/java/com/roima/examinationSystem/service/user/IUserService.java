package com.roima.examinationSystem.service.user;


import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {

    void addUser(AddUserRequest user) throws InvalidValueException, ResourceExistsException;
    void updateUser(UpdateUserRequest user, int userId) throws InvalidValueException, ResourceNotFoundException;
    void deleteUserById(int id) throws ResourceNotFoundException;

    List<User> getAllUsers();
    User getUserById(int id) throws ResourceNotFoundException;
    User getUserByEmail(String email) throws ResourceNotFoundException;
    List<User> getUsersByRole(String role) throws InvalidValueException;
}
