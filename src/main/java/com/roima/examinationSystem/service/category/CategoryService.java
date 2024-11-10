package com.roima.examinationSystem.service.category;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {


    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) throws ResourceNotFoundException {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void addCategory(String name) throws ResourceExistsException {

        boolean isPresentedName = categoryRepository.existsByName(name);
        if(isPresentedName){
            throw new ResourceExistsException("Category already exists!");
        }
        Category category = new Category(name);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(int id) throws ResourceNotFoundException {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public void updateCategory(String name, int id) throws ResourceNotFoundException, ResourceExistsException {

        Category category = getCategoryById(id);

        if(!(category.getName().equals(name)) && categoryRepository.existsByName(name)){
            throw new ResourceExistsException("Category already exists!");
        }

        category.setName(name);
        categoryRepository.save(category);
    }
}
