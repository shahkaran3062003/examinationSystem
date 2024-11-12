package com.roima.examinationSystem.service.programmingTestCase;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ProgrammingTestCase;
import com.roima.examinationSystem.request.AddProgrammingTestCaseRequest;
import com.roima.examinationSystem.request.UpdateProgrammingTestRequest;

import java.util.List;

public interface IProgrammingTestCaseService {


    ProgrammingTestCase getProgrammingTestCaseById(int id) throws ResourceNotFoundException;
    List<ProgrammingTestCase> getAllProgrammingTestCasesByProgrammingQuestionsId(int programmingQuestionsId) throws ResourceNotFoundException;

    void addProgrammingTestCase(AddProgrammingTestCaseRequest request) throws ResourceNotFoundException;
    void updateProgrammingTestCase(UpdateProgrammingTestRequest request, int id) throws ResourceNotFoundException;
    void deleteProgrammingTestCase(int id) throws ResourceNotFoundException;


}
