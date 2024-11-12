package com.roima.examinationSystem.service.mcqQuestions;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.InvalidNumberException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.model.QuestionType;
import com.roima.examinationSystem.request.AddMcqQuestionRequest;
import com.roima.examinationSystem.request.UpdateMcqQuestionRequest;

import java.util.List;

public interface IMcqQuestionsService {

    void addMcqQuestions(AddMcqQuestionRequest mcqQuestions) throws ResourceNotFoundException, InvalidENUMException,InvalidNumberException;
    void updateMcqQuestions(UpdateMcqQuestionRequest mcqQuestions, int id) throws ResourceNotFoundException, InvalidENUMException,InvalidNumberException;
    void deleteMcqQuestions(int id) throws ResourceNotFoundException;

    List<McqQuestions> getAllMcqQuestions();
    McqQuestions getMcqQuestionsById(int id) throws ResourceNotFoundException;

    List<McqQuestions> getAllMcqQuestionsByCategory(int category_id) throws ResourceNotFoundException;
    List<McqQuestions> getAllMcqQuestionsByCategoryQuestionType();

    List<McqQuestions> getAllMcqQuestionsByDifficulty(String difficulty) throws InvalidENUMException;

    List<McqQuestions> getAllMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidENUMException, ResourceNotFoundException;

    List<McqQuestions> getRandomMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidENUMException, ResourceNotFoundException, InvalidNumberException;


}
