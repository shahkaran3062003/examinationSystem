package com.roima.examinationSystem.controller;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.questionManagement.QuestionManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/mcq-options")
public class McqOptionsController {

    private final QuestionManagementService questionManagementService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqOptions( @PathVariable int id) {
        try{
            questionManagementService.deleteMcqOptions(id);
            return ResponseEntity.ok(new ApiResponse("Success", "McqOption deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
