package com.roima.examinationSystem.controller;

import com.roima.examinationSystem.exception.InvalidRoleException;
import com.roima.examinationSystem.exception.UserExistsException;
import com.roima.examinationSystem.exception.UserNotFoundException;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.user.IUserService;
import com.roima.examinationSystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {

    private final IUserService userService;
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse("success", userService.getAllUsers()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable int id) {

        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("success", user));
        }catch (UserNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam(name = "email") String email) {

        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(new ApiResponse("success", user));
        }catch (UserNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @GetMapping("get/role")
    public ResponseEntity<ApiResponse> getUsersByRole(@RequestParam(name = "role") String role) {
        try{
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(new ApiResponse("success", users));
        } catch (InvalidRoleException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("success", "User deleted successfully!"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody AddUserRequest request) {
        try {
            userService.addUser(request);
            return ResponseEntity.ok(new ApiResponse("success", "User added successfully!"));
        }catch (Exception | InvalidRoleException | UserExistsException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable int userId) {
        try {
            userService.updateUser(request,userId);
            return ResponseEntity.ok(new ApiResponse("success", "User updated successfully!"));
        }  catch (InvalidRoleException | UserNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }

    }
}
