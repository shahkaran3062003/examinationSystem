package com.roima.examinationSystem.service.admin.userCollegeManagement;

import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.dto.UserDto;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.LoginLog;
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
    List<UserDto> getAllUsers();
    UserDto getUserById(int id) throws ResourceNotFoundException;
    UserDto getUserByEmail(String email) throws ResourceNotFoundException;
    List<UserDto> getUsersByRole(String role) throws InvalidValueException;


    //College
    List<CollegeDto> getAllColleges();
    CollegeDto getCollegeById(int id) throws ResourceNotFoundException;
    CollegeDto getCollegeByName(String name) throws ResourceNotFoundException;
    void addCollege(AddCollegeRequest college) throws ResourceExistsException;
    void updateCollege(UpdateCollegeRequest college, int id) throws ResourceExistsException, ResourceNotFoundException;
    void deleteCollegeById(int id) throws ResourceNotFoundException;


    //Logging
    List<LoginLog> getAllLoginLogs();
    List<LoginLog> getLoginLogsByUserId(int userId);

    void deleteLoginLogsByUserId(int userId) throws ResourceNotFoundException;
    void deleteLoginLogsById(int id) throws ResourceNotFoundException;


}
