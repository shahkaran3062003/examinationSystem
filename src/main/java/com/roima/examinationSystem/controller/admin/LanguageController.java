package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddLanguageRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import com.roima.examinationSystem.service.judge0.Judge0Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.adminPrefix}/language")
@RequiredArgsConstructor
public class LanguageController {

    private final QuestionManagementService questionManagementService;
    private final Judge0Service judge0Service;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllLanguages() {
        return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllLanguages()));
    }

    @GetMapping("/get/judge0-languages")
    public ResponseEntity<ApiResponse> getJudge0Languages() {
        try{
            return ResponseEntity.ok(new ApiResponse("success", judge0Service.getAllJudge0Languages()));
        } catch (FetchException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addLanguage(@RequestBody @Valid AddLanguageRequest request) {
        try{
            questionManagementService.addLanguage(request);
            return ResponseEntity.ok(new ApiResponse("success", "Language added successfully!"));
        } catch (ResourceExistsException | ResourceNotFoundException  e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteLanguage(@PathVariable int id) {
        try{
            questionManagementService.deleteLanguage(id);
            return ResponseEntity.ok(new ApiResponse("success", "Language deleted successfully!"));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }



}
