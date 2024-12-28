package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddProgrammingTestCaseRequest;
import com.roima.examinationSystem.request.UpdateProgrammingTestRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/programming-test-case")
public class ProgrammingTestCaseController {

    private final QuestionManagementService questionManagementService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getProgrammingTestCaseById(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getProgrammingTestCaseById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/get/programming-question/{id}")
    public ResponseEntity<ApiResponse> getProgrammingTestCaseByProgrammingQuestionId(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllProgrammingTestCasesByProgrammingQuestionsId(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProgrammingTestCase(@RequestBody @Valid AddProgrammingTestCaseRequest request) {
        try{
            questionManagementService.addProgrammingTestCase(request);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case added successfully!"));
        } catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProgrammingTestCase(@RequestBody @Valid UpdateProgrammingTestRequest request, @PathVariable int id) {
        try{
            questionManagementService.updateProgrammingTestCase(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case updated successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProgrammingTestCase(@PathVariable int id) {
        try{
            questionManagementService.deleteProgrammingTestCase(id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }
}
