package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddUpdateCategoryRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.questionManagement.QuestionManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.adminPrefix}/category")
public class CategoryController {

    private final QuestionManagementService questionManagementService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllCategories()));
    }

    @GetMapping("/get/all/questionType")
    public ResponseEntity<ApiResponse> getAllQuestionTypes(@RequestParam("questionType") String questionType) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getAllCategoriesByQuestionType(questionType)));
        }catch(InvalidValueException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", questionManagementService.getCategoryById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid AddUpdateCategoryRequest request) {
        try{
            questionManagementService.addCategory(request);
            return ResponseEntity.ok(new ApiResponse("success", "Category added successfully!"));
        } catch (ResourceExistsException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid AddUpdateCategoryRequest request, @PathVariable int id) {
        try{
            questionManagementService.updateCategory(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Category updated successfully!"));
        } catch (ResourceNotFoundException | ResourceExistsException | InvalidValueException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id) {
        try{
            questionManagementService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Category deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
