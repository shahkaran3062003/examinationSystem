package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/mcq-questions")
public class McqQuestionsController {

    private final QuestionManagementService questionManagementService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllMcqQuestions() {
        return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllMcqQuestions()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getMcqQuestionById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getMcqQuestionsById(id)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category/{id}")
    public ResponseEntity<ApiResponse> getMcqQuestionsByCategory(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllMcqQuestionsByCategory(id)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/difficulty")
    public ResponseEntity<ApiResponse> getMcqQuestionsByDifficulty(@RequestParam("difficulty") String difficulty) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllMcqQuestionsByDifficulty(difficulty)));
        }catch (InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category-difficulty")
    public ResponseEntity<ApiResponse> getMcqQuestionsByCategoryAndDifficulty(@RequestParam("category") int category, @RequestParam("difficulty") String difficulty) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllMcqQuestionsByDifficultyAndCategory(difficulty,category)));
        }catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMcqQuestion(
            @RequestPart("question") @Valid String request,
            @RequestPart(value = "questionImage", required = false) MultipartFile questionImage,
            @RequestPart(value = "optionImages", required = false) List<MultipartFile> optionImages
            ) {
        try{
            questionManagementService.addMcqQuestions(request, questionImage, optionImages);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question added successfully!"));
        } catch (ResourceNotFoundException | InvalidValueException | IOException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMcqQuestion(
            @RequestPart("question") @Valid String requestJson,
            @RequestPart(value = "questionImage", required = false) MultipartFile questionImage,
            @PathVariable int id
    ) {
        try {
            questionManagementService.updateMcqQuestions(requestJson,questionImage, id);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question updated successfully!"));
        } catch (ResourceNotFoundException | InvalidValueException | IOException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqQuestion(@PathVariable int id) {
        try{
            questionManagementService.deleteMcqQuestions(id);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Question deleted successfully!"));
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


}
