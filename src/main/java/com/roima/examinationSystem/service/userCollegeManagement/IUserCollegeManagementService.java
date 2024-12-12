package com.roima.examinationSystem.service.userCollegeManagement;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;

import java.util.List;

public interface IUserCollegeManagementService {

    //User
    void addUser(AddUserRequest user) throws InvalidValueException, ResourceExistsException;
    void updateUser(UpdateUserRequest user, int userId) throws InvalidValueException, ResourceNotFoundException;
    void deleteUserById(int id) throws ResourceNotFoundException;
    List<User> getAllUsers();
    User getUserById(int id) throws ResourceNotFoundException;
    User getUserByEmail(String email) throws ResourceNotFoundException;
    List<User> getUsersByRole(String role) throws InvalidValueException;


    //College
    List<College> getAllColleges();
    College getCollegeById(int id) throws ResourceNotFoundException;
    College getCollegeByName(String name) throws ResourceNotFoundException;
    void addCollege(AddCollegeRequest college) throws ResourceExistsException;
    void updateCollege(UpdateCollegeRequest college, int id) throws ResourceExistsException, ResourceNotFoundException;
    void deleteCollegeById(int id) throws ResourceNotFoundException;

}
