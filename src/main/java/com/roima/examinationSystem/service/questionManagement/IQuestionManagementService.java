package com.roima.examinationSystem.service.questionManagement;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.request.*;

import java.util.List;

public interface IQuestionManagementService {

    // Category
    List<Category> getAllCategories();
    List<Category> getAllCategoriesByQuestionType(String questionType) throws InvalidValueException;
    Category getCategoryById(int id) throws ResourceNotFoundException;

    void addCategory(AddUpdateCategoryRequest name) throws ResourceExistsException, InvalidValueException;
    void deleteCategoryById(int id) throws ResourceNotFoundException;
    void updateCategory(AddUpdateCategoryRequest name, int id) throws ResourceNotFoundException, ResourceExistsException, InvalidValueException;


    // MCQ Questions
    void addMcqQuestions(AddMcqQuestionRequest mcqQuestions) throws ResourceNotFoundException, InvalidValueException;
    void updateMcqQuestions(UpdateMcqQuestionRequest mcqQuestions, int id) throws ResourceNotFoundException, InvalidValueException;
    void deleteMcqQuestions(int id) throws ResourceNotFoundException;

    List<McqQuestions> getAllMcqQuestions();
    McqQuestions getMcqQuestionsById(int id) throws ResourceNotFoundException;

    List<McqQuestions> getAllMcqQuestionsByCategory(int category_id) throws ResourceNotFoundException;
    List<McqQuestions> getAllMcqQuestionsByCategoryQuestionType();

    List<McqQuestions> getAllMcqQuestionsByDifficulty(String difficulty) throws InvalidValueException;

    List<McqQuestions> getAllMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidValueException, ResourceNotFoundException;

    List<McqQuestions> getRandomMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidValueException, ResourceNotFoundException;


    // Mcq Options
    McqOptions addMcqOptions(int option_number, String text);
    void deleteMcqOptions(int id) throws ResourceNotFoundException;
    void updateMcqOptions(int option_number,String text, int id) throws ResourceNotFoundException;


    // Programming Questions
    void addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidValueException, ResourceNotFoundException;
    void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidValueException;
    void deleteProgrammingQuestions(int id) throws ResourceNotFoundException;


    List<ProgrammingQuestions> getAllProgrammingQuestions();
    ProgrammingQuestions getProgrammingQuestionsById(int id) throws ResourceNotFoundException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByCategory(int category_id) throws ResourceNotFoundException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByCategoryQuestionType();
    List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficulty(String difficulty) throws InvalidValueException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidValueException, ResourceNotFoundException;
    List<ProgrammingQuestions> getRandomProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id,int number) throws InvalidValueException, ResourceNotFoundException;


    // Programming Test Cases
    ProgrammingTestCase getProgrammingTestCaseById(int id) throws ResourceNotFoundException;
    List<ProgrammingTestCase> getAllProgrammingTestCasesByProgrammingQuestionsId(int programmingQuestionsId) throws ResourceNotFoundException;
    void addProgrammingTestCase(AddProgrammingTestCaseRequest request) throws ResourceNotFoundException;
    void updateProgrammingTestCase(UpdateProgrammingTestRequest request, int id) throws ResourceNotFoundException;
    void deleteProgrammingTestCase(int id) throws ResourceNotFoundException;


}
