package com.roima.examinationSystem.service.examMcqDetails;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.ExamCategoryDetails;

import java.util.List;

public interface IExamMcqDetailsService {

    List<ExamCategoryDetails> getAllExamCategory();
    void getExamCategoryById(int id) throws ResourceNotFoundException;

    void addExamCategory(ExamCategoryDetails examCategoryDetails);
    void deleteExamCategory(int id) throws ResourceNotFoundException;
    void updateExamCategory(ExamCategoryDetails examCategoryDetails) throws ResourceNotFoundException;
}
