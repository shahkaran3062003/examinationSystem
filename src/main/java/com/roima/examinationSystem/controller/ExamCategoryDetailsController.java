package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.examCategoryDetails.ExamCategoryDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/exam-category-details")
public class ExamCategoryDetailsController {

    private final ExamCategoryDetailsService examCategoryDetailsService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllExamCategory() {
        return ResponseEntity.ok(new ApiResponse("success", examCategoryDetailsService.getAllExamCategory()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getExamCategoryById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examCategoryDetailsService.getExamCategoryById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/exam/{examId}")
    public ResponseEntity<ApiResponse> getExamCategoryByExamId(@PathVariable int examId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examCategoryDetailsService.getExamCategoryByExamId(examId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExamCategory(@RequestBody @Valid AddUpdateExamCategoryDetailsRequest request) {
        try{
            examCategoryDetailsService.addExamCategory(request);
        return ResponseEntity.ok(new ApiResponse("success","Exam Category added successfully" ));
        }catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateExamCategory(@RequestBody @Valid AddUpdateExamCategoryDetailsRequest request, @PathVariable int id) {
        try{
            examCategoryDetailsService.updateExamCategory(request, id);
            return ResponseEntity.ok(new ApiResponse("success","Exam Category updated successfully" ));
        }catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteExamCategory(@PathVariable int id) {
        try{
            examCategoryDetailsService.deleteExamCategory(id);
            return ResponseEntity.ok(new ApiResponse("success","Exam Category deleted successfully" ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
