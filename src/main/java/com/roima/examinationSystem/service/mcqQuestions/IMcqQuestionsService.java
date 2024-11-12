package com.roima.examinationSystem.service.mcqQuestions;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.request.AddMcqQuestionRequest;
import com.roima.examinationSystem.request.UpdateMcqQuestionRequest;

import java.util.List;

public interface IMcqQuestionsService {

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


}
