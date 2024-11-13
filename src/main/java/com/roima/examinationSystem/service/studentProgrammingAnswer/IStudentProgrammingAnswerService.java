package com.roima.examinationSystem.service.studentProgrammingAnswer;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.StudentProgrammingAnswer;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;

import java.util.List;

public interface IStudentProgrammingAnswerService {
    void addStudentProgrammingAnswer(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException;
    List<StudentProgrammingAnswer> getAllProgrammingAnswerByStudentId(int studentId) throws ResourceNotFoundException;
    StudentProgrammingAnswer getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;

    void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;
}
