package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddCategoryRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/category")
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        return ResponseEntity.ok(new ApiResponse("success", categoryService.getAllCategories()));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", categoryService.getCategoryById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid AddCategoryRequest request) {
        try{
            categoryService.addCategory(request.getName());
            return ResponseEntity.ok(new ApiResponse("success", "Category added successfully!"));
        } catch (ResourceExistsException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid AddCategoryRequest request,@PathVariable int id) {
        try{
            categoryService.updateCategory(request.getName(), id);
            return ResponseEntity.ok(new ApiResponse("success", "Category updated successfully!"));
        } catch (ResourceNotFoundException | ResourceExistsException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id) {
        try{
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Category deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
