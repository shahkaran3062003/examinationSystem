package com.roima.examinationSystem.service.programmingQuestions;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.InvalidNumberException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.request.AddProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.UpdateProgrammingQuestionsRequest;

import java.util.List;

public interface IProgrammingQuestionsService {

    void addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidENUMException, ResourceNotFoundException;
    void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidENUMException;
    void deleteProgrammingQuestions(int id) throws ResourceNotFoundException;


    List<ProgrammingQuestions> getAllProgrammingQuestions();
    ProgrammingQuestions getProgrammingQuestionsById(int id) throws ResourceNotFoundException;

    List<ProgrammingQuestions> getAllProgrammingQuestionsByCategory(int category_id) throws ResourceNotFoundException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByCategoryQuestionType();

    List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficulty(String difficulty) throws InvalidENUMException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidENUMException, ResourceNotFoundException;

    List<ProgrammingQuestions> getRandomProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id,int number) throws InvalidENUMException, ResourceNotFoundException, InvalidNumberException;

}
