package com.roima.examinationSystem.service.examCategoryDetails;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ExamCategoryDetails;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;

import java.util.List;

public interface IExamCategoryDetailsService {

    List<ExamCategoryDetails> getAllExamCategory();
    ExamCategoryDetails getExamCategoryById(int id) throws ResourceNotFoundException;

    List<ExamCategoryDetails> getExamCategoryByExamId(int examId) throws ResourceNotFoundException;

    void addExamCategory(AddUpdateExamCategoryDetailsRequest request) throws ResourceNotFoundException, InvalidValueException;

    void updateExamCategory(AddUpdateExamCategoryDetailsRequest request, int id) throws ResourceNotFoundException, InvalidValueException;

    void deleteExamCategory(int id) throws ResourceNotFoundException;
}
