package com.roima.examinationSystem.service.Category;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.QuestionType;
import com.roima.examinationSystem.request.AddCategoryRequest;

import java.util.List;

public interface ICategoryService {

    List<Category> getAllCategories();
    List<Category> getAllCategoriesByQuestionType(String questionType) throws InvalidENUMException;
    Category getCategoryById(int id) throws ResourceNotFoundException;

    void addCategory(AddCategoryRequest name) throws ResourceExistsException, InvalidENUMException;
    void deleteCategoryById(int id) throws ResourceNotFoundException;
    void updateCategory(AddCategoryRequest name, int id) throws ResourceNotFoundException, ResourceExistsException, InvalidENUMException;
}
