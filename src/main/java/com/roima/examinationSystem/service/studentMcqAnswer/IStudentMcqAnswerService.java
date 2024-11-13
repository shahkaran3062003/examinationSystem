package com.roima.examinationSystem.service.studentMcqAnswer;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;

import java.util.List;

public interface IStudentMcqAnswerService {

    void addStudentMcqAnswer(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException;
    List<StudentMcqAnswer> getStudentMcqAnswerByStudentId(int studentId) throws ResourceNotFoundException;

    StudentMcqAnswer getStudentMcqAnswerById(int id) throws ResourceNotFoundException;
    void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException;
}
