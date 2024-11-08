package com.roima.examinationSystem.service.user;


import com.roima.examinationSystem.exception.InvalidRoleException;
import com.roima.examinationSystem.exception.UserExistsException;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {

    void addUser(AddUserRequest user) throws InvalidRoleException, UserExistsException;
    void updateUser(UpdateUserRequest user, int userId) throws InvalidRoleException;
    void deleteUserById(int id);

    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByEmail(String email);
    List<User> getUsersByRole(String role) throws InvalidRoleException;
}
