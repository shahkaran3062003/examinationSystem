package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.dto.student.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.studentManagement.StudentManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/student")
public class StudentController {

    private final StudentManagementService studentManagementService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllStudents() {
        return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getAllStudents()));
    }


    @GetMapping("/get/all/college/{id}")
    public ResponseEntity<ApiResponse> getAllStudentsByCollegeId(@PathVariable int id) {

        return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentsByCollegeId(id)));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable int id) {

        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getStudentByEmail(@RequestParam(name = "email") String email) {

        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentByEmail(email)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody @Valid AddStudentRequest request) {
        try {

            studentManagementService.addStudent(request);
            return ResponseEntity.ok(new ApiResponse("success", "Student added successfully!"));
        } catch (Exception | ResourceNotFoundException | ResourceExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@RequestBody @Valid UpdateStudentRequest request, @PathVariable int id) {
        try {
            studentManagementService.updateStudent(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Student updated successfully!"));
        } catch (Exception | ResourceNotFoundException | ResourceExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable int id) {
        try {
            studentManagementService.deleteStudentById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Student deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
