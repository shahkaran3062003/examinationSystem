package com.roima.examinationSystem.service.programmingQuestions;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.request.AddProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.UpdateProgrammingQuestionsRequest;

import java.util.List;

public interface IProgrammingQuestionsService {

    ProgrammingQuestions addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidENUMException, ResourceNotFoundException;
    void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidENUMException;
    void deleteProgrammingQuestions(int id) throws ResourceNotFoundException;


    List<ProgrammingQuestions> getAllProgrammingQuestions();
    ProgrammingQuestions getProgrammingQuestionsById(int id) throws ResourceNotFoundException;
    List<ProgrammingQuestions> getProgrammingQuestionsByDifficulty(String difficulty) throws InvalidENUMException;
}
