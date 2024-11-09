package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.dto.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/student")
public class StudentController {


    private final StudentService studentService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllStudents() {

        List<Student> students = studentService.getAllStudents();
        List<StudentDto> studentDtos = studentService.getConvertedDtoList(students);
        return ResponseEntity.ok(new ApiResponse("success", studentDtos));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable int id) {

        try {
            Student student = studentService.getStudentById(id);
            StudentDto studentDto = studentService.convertToDto(student);
            return ResponseEntity.ok(new ApiResponse("success", studentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getStudentByEmail(@RequestParam(name = "email") String email) {

        try {
            Student student = studentService.getStudentByEmail(email);
            StudentDto studentDto = studentService.convertToDto(student);
            return ResponseEntity.ok(new ApiResponse("success", studentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody @Valid AddStudentRequest request) {
        try {

            studentService.addStudent(request);
            return ResponseEntity.ok(new ApiResponse("success", "Student added successfully!"));
        } catch (Exception | ResourceNotFoundException | ResourceExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@RequestBody @Valid UpdateStudentRequest request, @PathVariable int id) {
        try {
            studentService.updateStudent(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Student updated successfully!"));
        } catch (Exception | ResourceNotFoundException | ResourceExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable int id) {
        try {
            studentService.deleteStudentById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Student deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
