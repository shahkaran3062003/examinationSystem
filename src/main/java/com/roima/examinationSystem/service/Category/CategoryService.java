package com.roima.examinationSystem.service.Category;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.QuestionType;
import com.roima.examinationSystem.repository.CategoryRepository;
import com.roima.examinationSystem.request.AddUpdateCategoryRequest;
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
    public List<Category> getAllCategoriesByQuestionType(String questionType) throws InvalidValueException {
        try{
            QuestionType questionTypeE = QuestionType.valueOf(questionType);
            return categoryRepository.findAllByQuestionType(questionTypeE);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }
    }

    @Override
    public Category getCategoryById(int id) throws ResourceNotFoundException {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void addCategory(AddUpdateCategoryRequest request) throws ResourceExistsException, InvalidValueException {

        try {
            boolean isPresentedName = categoryRepository.existsByName(request.getName());
            if (isPresentedName) {
                throw new ResourceExistsException("Category already exists!");
            }

            QuestionType questionType = QuestionType.valueOf(request.getQuestionType());

            Category category = new Category(request.getName(), questionType);
            categoryRepository.save(category);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }
    }

    @Override
    public void deleteCategoryById(int id) throws ResourceNotFoundException {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public void updateCategory(AddUpdateCategoryRequest request, int id) throws ResourceNotFoundException, ResourceExistsException, InvalidValueException {

        Category category = getCategoryById(id);

        if(!(category.getName().equals(request.getName())) && categoryRepository.existsByName(request.getName())){
            throw new ResourceExistsException("Category already exists!");
        }

        try{
            QuestionType questionType = QuestionType.valueOf(request.getQuestionType());
            category.setQuestionType(questionType);
            category.setName(request.getName());
            categoryRepository.save(category);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }

    }
}
