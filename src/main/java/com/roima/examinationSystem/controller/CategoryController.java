package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddCategoryRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/category")
public class CategoryController {
    private final CategoryService CategoryService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        return ResponseEntity.ok(new ApiResponse("success", CategoryService.getAllCategories()));
    }

    @GetMapping("/get/all/questionType")
    public ResponseEntity<ApiResponse> getAllQuestionTypes(@RequestParam("questionType") String questionType) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", CategoryService.getAllCategoriesByQuestionType(questionType)));
        }catch(InvalidENUMException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", CategoryService.getCategoryById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid AddCategoryRequest request) {
        try{
            CategoryService.addCategory(request);
            return ResponseEntity.ok(new ApiResponse("success", "Category added successfully!"));
        } catch (ResourceExistsException | InvalidENUMException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid AddCategoryRequest request, @PathVariable int id) {
        try{
            CategoryService.updateCategory(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Category updated successfully!"));
        } catch (ResourceNotFoundException | ResourceExistsException | InvalidENUMException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id) {
        try{
            CategoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Category deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
