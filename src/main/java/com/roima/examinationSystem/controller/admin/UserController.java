package com.roima.examinationSystem.controller.admin;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.userCollegeManagement.UserCollegeManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.adminPrefix}/user")
public class UserController {

    private final UserCollegeManagementService userCollegeManagementService;



    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse("success", userCollegeManagementService.getAllUsers()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable int id) {

        try {
            User user = userCollegeManagementService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("success", user));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam(name = "email") String email) {

        try {
            User user = userCollegeManagementService.getUserByEmail(email);
            return ResponseEntity.ok(new ApiResponse("success", user));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @GetMapping("get/role")
    public ResponseEntity<ApiResponse> getUsersByRole(@RequestParam(name = "role") String role) {
        try{
            List<User> users = userCollegeManagementService.getUsersByRole(role);
            return ResponseEntity.ok(new ApiResponse("success", users));
        } catch (InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable int id) {
        try {
            userCollegeManagementService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("success", "User deleted successfully!"));
        }catch (Exception | ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody @Valid AddUserRequest request) {
        try {
            userCollegeManagementService.addUser(request);
            return ResponseEntity.ok(new ApiResponse("success", "User added successfully!"));
        }catch (Exception | InvalidValueException | ResourceExistsException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody @Valid UpdateUserRequest request, @PathVariable int userId) {
        try {
            userCollegeManagementService.updateUser(request,userId);
            return ResponseEntity.ok(new ApiResponse("success", "User updated successfully!"));
        }  catch (InvalidValueException | ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }
}
