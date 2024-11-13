package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.studentMcqAnswer.StudentMcqAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/student-mcq-answer")
public class StudentMcqAnswerController {

    private final StudentMcqAnswerService studentMcqAnswerService;


    @GetMapping("get/all/student/{studentId}")
    public ResponseEntity<ApiResponse> getAllMcqAnswerByStudentId(@PathVariable int studentId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentMcqAnswerService.getStudentMcqAnswerByStudentId(studentId)));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse> getMcqAnswerById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentMcqAnswerService.getStudentMcqAnswerById(id)));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMcqAnswer(@RequestBody AddStudentMcqAnswerRequest request) {
        try {
            studentMcqAnswerService.addStudentMcqAnswer(request);
            return ResponseEntity.ok(new ApiResponse("success","Mcq Answer added successfully" ));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqAnswer(@PathVariable int id) {
        try {
            studentMcqAnswerService.deleteStudentMcqAnswerById(id);
            return ResponseEntity.ok(new ApiResponse("success","Mcq Answer deleted successfully" ));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
