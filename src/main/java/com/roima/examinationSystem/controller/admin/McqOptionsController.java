package com.roima.examinationSystem.controller.admin;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/mcq-options")
public class McqOptionsController {

    private final QuestionManagementService questionManagementService;



    @PostMapping("add")
    public ResponseEntity<ApiResponse> addMcqOption(
            @RequestPart("option") @Valid String requestJson,
            @RequestPart(value = "optionImage", required = false)MultipartFile optionImage
            ){
        try{
            questionManagementService.addMcqOptions(requestJson,optionImage);
            return ResponseEntity.ok(new ApiResponse("success","Mcq option added successfully."));
        } catch (ResourceNotFoundException | IOException | ResourceExistsException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMcqOption(
            @RequestPart("option") @Valid String requestJson,
            @RequestPart(value = "optionImage", required = false)MultipartFile optionImage,
            @PathVariable int id
    ){
        try{
            questionManagementService.updateMcqOptions(requestJson,optionImage,id);
            return ResponseEntity.ok(new ApiResponse("success","Mcq option updated successfully."));
        } catch (ResourceNotFoundException | InvalidValueException | ResourceExistsException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }

    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqOptions( @PathVariable int id) {
        try{
            questionManagementService.deleteMcqOptions(id);
            return ResponseEntity.ok(new ApiResponse("Success", "McqOption deleted successfully!"));
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
