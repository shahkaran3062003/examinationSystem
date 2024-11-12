package com.roima.examinationSystem.service.Category;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.request.AddUpdateCategoryRequest;

import java.util.List;

public interface ICategoryService {

    List<Category> getAllCategories();
    List<Category> getAllCategoriesByQuestionType(String questionType) throws InvalidValueException;
    Category getCategoryById(int id) throws ResourceNotFoundException;

    void addCategory(AddUpdateCategoryRequest name) throws ResourceExistsException, InvalidValueException;
    void deleteCategoryById(int id) throws ResourceNotFoundException;
    void updateCategory(AddUpdateCategoryRequest name, int id) throws ResourceNotFoundException, ResourceExistsException, InvalidValueException;
}
