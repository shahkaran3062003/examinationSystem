package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddProgrammingTestCaseRequest;
import com.roima.examinationSystem.request.UpdateProgrammingTestCaseRequest;
import com.roima.examinationSystem.request.UpdateProgrammingTestRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.programmingTestCase.ProgrammingTestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/programming-test-case")
public class ProgrammingTestCaseController {

    private final ProgrammingTestCaseService programmingTestCaseService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getProgrammingTestCaseById(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", programmingTestCaseService.getProgrammingTestCaseById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/get/programming-question/{id}")
    public ResponseEntity<ApiResponse> getProgrammingTestCaseByProgrammingQuestionId(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", programmingTestCaseService.getAllProgrammingTestCasesByProgrammingQuestionsId(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProgrammingTestCase(@RequestBody @Valid AddProgrammingTestCaseRequest request) {
        try{
            programmingTestCaseService.addProgrammingTestCase(request);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case added successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProgrammingTestCase(@RequestBody @Valid UpdateProgrammingTestRequest request, @PathVariable int id) {
        try{
            programmingTestCaseService.updateProgrammingTestCase(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case updated successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProgrammingTestCase(@PathVariable int id) {
        try{
            programmingTestCaseService.deleteProgrammingTestCase(id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Test Case deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error",e.getMessage()));
        }
    }
}
