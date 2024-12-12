package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.dto.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.studentManagement.StudentManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/student")
public class StudentController {

    private final StudentManagementService studentManagementService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllStudents() {

        List<Student> students = studentManagementService.getAllStudents();
        List<StudentDto> studentDtos = studentManagementService.getConvertedDtoList(students);
        return ResponseEntity.ok(new ApiResponse("success", studentDtos));
    }


    @GetMapping("/get/all/college/{id}")
    public ResponseEntity<ApiResponse> getAllStudentsByCollegeId(@PathVariable int id) {

        List<Student> students = studentManagementService.getStudentsByCollegeId(id);
        List<StudentDto> studentDtos = studentManagementService.getConvertedDtoList(students);
        return ResponseEntity.ok(new ApiResponse("success", studentDtos));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable int id) {

        try {
            Student student = studentManagementService.getStudentById(id);
            StudentDto studentDto = studentManagementService.convertToDto(student);
            return ResponseEntity.ok(new ApiResponse("success", studentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getStudentByEmail(@RequestParam(name = "email") String email) {

        try {
            Student student = studentManagementService.getStudentByEmail(email);
            StudentDto studentDto = studentManagementService.convertToDto(student);
            return ResponseEntity.ok(new ApiResponse("success", studentDto));
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
