package com.roima.examinationSystem.service.programmingTestCase;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.model.ProgrammingTestCase;
import com.roima.examinationSystem.repository.ProgrammingQuestionsRepository;
import com.roima.examinationSystem.repository.ProgrammingTestCaseRepository;
import com.roima.examinationSystem.request.AddProgrammingTestCaseRequest;
import com.roima.examinationSystem.request.UpdateProgrammingTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProgrammingTestCaseService implements IProgrammingTestCaseService {

    private final ProgrammingTestCaseRepository programmingTestCaseRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;

    @Override
    public ProgrammingTestCase getProgrammingTestCaseById(int id) throws ResourceNotFoundException {
        return programmingTestCaseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Programming Test Case not found!"));
    }

    @Override
    public List<ProgrammingTestCase> getAllProgrammingTestCasesByProgrammingQuestionsId(int programmingQuestionsId) throws ResourceNotFoundException {
        if(!programmingQuestionsRepository.existsById(programmingQuestionsId)){
            throw new ResourceNotFoundException("Programming Questions not found!");
        }

        return programmingTestCaseRepository.findAllByProgrammingQuestionsId(programmingQuestionsId);
    }

    @Override
    public void addProgrammingTestCase(AddProgrammingTestCaseRequest request) throws ResourceNotFoundException {

        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionId()).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));

        ProgrammingTestCase testCase = new ProgrammingTestCase(
                request.getInput(),
                request.getOutput(),
                programmingQuestions
        );
        programmingTestCaseRepository.save(testCase);
    }

    @Override
    public void updateProgrammingTestCase(UpdateProgrammingTestRequest request, int id) throws ResourceNotFoundException {
        ProgrammingTestCase testCase = programmingTestCaseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
        testCase.setInput(request.getInput());
        testCase.setOutput(request.getOutput());
        programmingTestCaseRepository.save(testCase);
    }

    @Override
    public void deleteProgrammingTestCase(int id) throws ResourceNotFoundException {
        ProgrammingTestCase testCase = programmingTestCaseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
        programmingTestCaseRepository.delete(testCase);

    }

}
