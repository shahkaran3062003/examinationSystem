package com.roima.examinationSystem.service.admin.questionManagement;

import com.roima.examinationSystem.dto.LanguageDto;
import com.roima.examinationSystem.dto.admin.AdminMcqQuestionDto;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.request.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    void addMcqQuestions(String request, MultipartFile questionImage, List<MultipartFile> optionImages) throws ResourceNotFoundException, InvalidValueException, IOException;
    void updateMcqQuestions(String requestJson,MultipartFile questionImage, int id) throws ResourceNotFoundException, InvalidValueException, IOException;
    void deleteMcqQuestions(int id) throws ResourceNotFoundException, IOException;

    List<AdminMcqQuestionDto> getAllMcqQuestions();
    AdminMcqQuestionDto getMcqQuestionsById(int id) throws ResourceNotFoundException;

    List<McqQuestions> getAllMcqQuestionsByCategory(int category_id) throws ResourceNotFoundException;
    List<McqQuestions> getAllMcqQuestionsByCategoryQuestionType();

    List<McqQuestions> getAllMcqQuestionsByDifficulty(String difficulty) throws InvalidValueException;

    List<McqQuestions> getAllMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidValueException, ResourceNotFoundException;

    List<McqQuestions> getRandomMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidValueException, ResourceNotFoundException;


    // Mcq Options
    void addMcqOptions(String requestJson, MultipartFile optionFile) throws IOException, ResourceNotFoundException, ResourceExistsException, InvalidValueException;
    void deleteMcqOptions(int id) throws ResourceNotFoundException, IOException;
    void updateMcqOptions(String requestJson,MultipartFile optionImage , int id) throws ResourceNotFoundException,InvalidValueException,ResourceExistsException;


    // Programming Questions
    void addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidValueException, ResourceNotFoundException, FetchException;
    void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidValueException, FetchException;
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
    void addProgrammingTestCase(AddProgrammingTestCaseRequest request) throws ResourceNotFoundException, InvalidValueException, FetchException;
    void updateProgrammingTestCase(UpdateProgrammingTestCaseRequest request, int id) throws ResourceNotFoundException, InvalidValueException, FetchException;
    void deleteProgrammingTestCase(int id) throws ResourceNotFoundException, InvalidValueException;

    // Programming Languages
    List<LanguageDto> getAllLanguages();
    void addLanguage(AddLanguageRequest request) throws ResourceExistsException, ResourceNotFoundException;
    void deleteLanguage(int id) throws ResourceNotFoundException;



}
