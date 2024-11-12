package com.roima.examinationSystem.service.programmingQuestions;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.CategoryRepository;
import com.roima.examinationSystem.repository.ProgrammingQuestionsRepository;
import com.roima.examinationSystem.repository.ProgrammingTestCaseRepository;
import com.roima.examinationSystem.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProgrammingQuestionsService implements IProgrammingQuestionsService {

    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ProgrammingTestCaseRepository programmingTestCaseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProgrammingQuestions addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidENUMException, ResourceNotFoundException {
        try{
            Difficulty difficultyE = Difficulty.valueOf(request.getDifficulty());

            Category category = categoryRepository.findByName(request.getCategory());

            if(category == null){
                category = new Category(request.getCategory(), QuestionType.PROGRAMMING);
                categoryRepository.save(category);
            }

            ProgrammingQuestions programmingQuestions = new ProgrammingQuestions(request.getStatement(), difficultyE, request.getImplementation(),category);
            programmingQuestionsRepository.save(programmingQuestions);

            for(AddProgrammingTestRequest testCase: request.getProgrammingTestCases()){
                ProgrammingTestCase programmingTestCase = new ProgrammingTestCase(testCase.getInput(), testCase.getOutput(),programmingQuestions);
                programmingTestCaseRepository.save(programmingTestCase);
            }

            return programmingQuestions;

        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        }
    }

    @Override
    public void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidENUMException {
        try{
            ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));

            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));

            for(UpdateProgrammingTestCaseRequest testCase: request.getProgrammingTestCases()){
                ProgrammingTestCase programmingTestCase = programmingTestCaseRepository.findById(testCase.getId()).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
                programmingTestCase.setInput(testCase.getInput());
                programmingTestCase.setOutput(testCase.getOutput());
                programmingTestCaseRepository.save(programmingTestCase);
            }

            programmingQuestions.setStatement(request.getStatement());
            programmingQuestions.setDifficulty(difficulty);
            programmingQuestions.setImplementation(request.getImplementation());
            programmingQuestions.setCategory(category);
            programmingQuestionsRepository.save(programmingQuestions);

        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        }
    }

    @Override
    public void deleteProgrammingQuestions(int id) throws ResourceNotFoundException {
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));
        programmingQuestionsRepository.delete(programmingQuestions);
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestions() {
        return programmingQuestionsRepository.findAll();
    }

    @Override
    public ProgrammingQuestions getProgrammingQuestionsById(int id) throws ResourceNotFoundException {
        return programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));
    }

    @Override
    public List<ProgrammingQuestions> getProgrammingQuestionsByDifficulty(String difficulty) throws InvalidENUMException {

        try {
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            return programmingQuestionsRepository.findAllByDifficulty(difficultyE);
        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        }
    }
}
