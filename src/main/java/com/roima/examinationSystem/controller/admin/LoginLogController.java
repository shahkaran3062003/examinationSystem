package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.userCollegeManagement.UserCollegeManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.adminPrefix}/login-log")
@RequiredArgsConstructor
public class LoginLogController {

    private final UserCollegeManagementService userCollegeManagementService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllLoginLogs() {
        return ResponseEntity.ok(new ApiResponse("success", userCollegeManagementService.getAllLoginLogs()));
    }


    @GetMapping("/get/user/{id}")
    public ResponseEntity<ApiResponse> getLoginLogsByStudentId(@PathVariable int id) {
        return ResponseEntity.ok(new ApiResponse("success", userCollegeManagementService.getLoginLogsByUserId(id)));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteLoginLog(@PathVariable int id) {
        try{
            userCollegeManagementService.deleteLoginLogsById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Login Log deleted successfully!"));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", "Invalid login log id"));
        }
    }

    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<ApiResponse> deleteLoginLogsByStudentId(@PathVariable int id) {
        try{
            userCollegeManagementService.deleteLoginLogsByUserId(id);
            return ResponseEntity.ok(new ApiResponse("success", "Login Log deleted successfully!"));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", "Invalid user id"));
        }
    }
}
