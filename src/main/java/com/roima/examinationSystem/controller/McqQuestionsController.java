package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.InvalidNumberException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddMcqQuestionRequest;
import com.roima.examinationSystem.request.UpdateMcqQuestionRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.mcqQuestions.McqQuestionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/mcq-questions")
public class McqQuestionsController {

    private final McqQuestionsService mcqQuestionsService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllMcqQuestions() {
        return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getAllMcqQuestions()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getMcqQuestionById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getMcqQuestionsById(id)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category/{id}")
    public ResponseEntity<ApiResponse> getMcqQuestionsByCategory(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getAllMcqQuestionsByCategory(id)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category/questionType")
    public ResponseEntity<ApiResponse> getMcqQuestionsByCategoryAndQuestionType() {
            return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getAllMcqQuestionsByCategoryQuestionType()));
    }

    @GetMapping("/get/difficulty")
    public ResponseEntity<ApiResponse> getMcqQuestionsByDifficulty(@RequestParam("difficulty") String difficulty) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getAllMcqQuestionsByDifficulty(difficulty)));
        }catch (InvalidENUMException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category-difficulty")
    public ResponseEntity<ApiResponse> getMcqQuestionsByCategoryAndDifficulty(@RequestParam("category") int category, @RequestParam("difficulty") String difficulty) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", mcqQuestionsService.getAllMcqQuestionsByDifficultyAndCategory(difficulty,category)));
        }catch (ResourceNotFoundException | InvalidENUMException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

//    TODO implement image with multipart form data
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMcqQuestion(@RequestBody @Valid AddMcqQuestionRequest request) {
        try{
            mcqQuestionsService.addMcqQuestions(request);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question added successfully!"));
        } catch (ResourceNotFoundException | InvalidENUMException | InvalidNumberException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMcqQuestion(@RequestBody @Valid UpdateMcqQuestionRequest request, @PathVariable int id) {
        try {
            mcqQuestionsService.updateMcqQuestions(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question updated successfully!"));
        } catch (ResourceNotFoundException | InvalidENUMException | InvalidNumberException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqQuestion(@PathVariable int id) {
        try{
            mcqQuestionsService.deleteMcqQuestions(id);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


}
