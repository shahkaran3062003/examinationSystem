package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.UpdateProgrammingQuestionsRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/programming-questions")
public class ProgrammingQuestionsController {

    private final QuestionManagementService questionManagementService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProgrammingQuestions() {
        return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingQuestions()));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getProgrammingQuestionById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getProgrammingQuestionsById(id)));

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get/category/{id}")
    public ResponseEntity<ApiResponse> getProgrammingQuestionsByCategory(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingQuestionsByCategory(id)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category/questionType")
    public ResponseEntity<ApiResponse> getProgrammingQuestionsByCategoryAndQuestionType() {
        return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingQuestionsByCategoryQuestionType()));
    }

    @GetMapping("/get/difficulty")
    public ResponseEntity<ApiResponse> getProgrammingQuestionsByDifficulty(@RequestParam(name = "difficulty") String difficulty){
        try{
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingQuestionsByDifficulty(difficulty)));
        } catch (InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/category-difficulty")
    public ResponseEntity<ApiResponse> getProgrammingQuestionsByCategoryAndDifficulty(@RequestParam("category") int category, @RequestParam("difficulty") String difficulty) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingQuestionsByDifficultyAndCategory(difficulty,category)));
        }catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProgrammingQuestion(@RequestBody @Valid AddProgrammingQuestionsRequest request) {
        try{
            questionManagementService.addProgrammingQuestions(request);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question added successfully!"));
        } catch (InvalidValueException | ResourceNotFoundException | FetchException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error",e.getMessage()));
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProgrammingQuestion(@RequestBody @Valid UpdateProgrammingQuestionsRequest request, @PathVariable int id)  {
        try{
            questionManagementService.updateProgrammingQuestions(request,id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question updated successfully!"));
        }catch(InvalidValueException | ResourceNotFoundException | FetchException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProgrammingQuestion(@PathVariable int id) {
        try{
            questionManagementService.deleteProgrammingQuestions(id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

}
