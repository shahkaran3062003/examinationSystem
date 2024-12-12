package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.studentManagement.StudentManagementService;
import com.roima.examinationSystem.service.studentProgrammingAnswer.StudentProgrammingAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/student-programming-answer")
public class StudentProgrammingAnswerController {

    private final StudentManagementService studentManagementService;

    @GetMapping("/get/student/{id}")
    public ResponseEntity<ApiResponse> getStudentProgrammingAnswerByStudentId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getAllProgrammingAnswerByStudentId(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getProgrammingAnswerById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getProgrammingAnswerById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProgrammingAnswer(@RequestBody @Valid AddStudentProgrammingAnswerRequest request) {
        try {
            studentManagementService.addStudentProgrammingAnswer(request);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Answer added successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProgrammingAnswer(@PathVariable int id) {
        try {
            studentManagementService.deleteProgrammingAnswerById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Answer deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
