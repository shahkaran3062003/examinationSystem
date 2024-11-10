package com.roima.examinationSystem.service.category;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> getAllCategories();
    Category getCategoryById(int id) throws ResourceNotFoundException;

    void addCategory(String name) throws ResourceExistsException;
    void deleteCategoryById(int id) throws ResourceNotFoundException;
    void updateCategory(String name, int id) throws ResourceNotFoundException, ResourceExistsException;
}
