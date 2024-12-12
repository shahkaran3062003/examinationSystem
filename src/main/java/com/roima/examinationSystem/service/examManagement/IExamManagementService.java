package com.roima.examinationSystem.service.examManagement;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.ExamCategoryDetails;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.request.AddDeleteExamMcqQuestionsRequest;
import com.roima.examinationSystem.request.AddDeleteExamProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;
import com.roima.examinationSystem.request.AddUpdateExamRequest;

import java.util.List;

public interface IExamManagementService {

    //exam
    List<Exam> getAllExams();
    Exam getExamById(int id) throws ResourceNotFoundException;
    List<Exam> getAllExamsByCollegeId(int id) throws ResourceNotFoundException;
    List<Exam> getAllExamsByDifficulty(String difficulty) throws ResourceNotFoundException, InvalidValueException;
    List<McqQuestions> getAllMcqQuestionsByExam(int examId) throws ResourceNotFoundException;
    List<ProgrammingQuestions> getAllProgrammingQuestionsByExam(int examId) throws ResourceNotFoundException;
    void addExam(AddUpdateExamRequest request) throws ResourceNotFoundException, InvalidValueException;
    void addExamMcqQuestions(AddDeleteExamMcqQuestionsRequest request, int examId) throws ResourceNotFoundException;
    void addProgrammingQuestions(AddDeleteExamProgrammingQuestionsRequest request, int examId) throws ResourceNotFoundException;
    void updateExam(AddUpdateExamRequest request, int id) throws ResourceNotFoundException, InvalidValueException;
    void deleteExam(int id) throws ResourceNotFoundException;
    void deleteExamMcqQuestions(AddDeleteExamMcqQuestionsRequest request,int examId) throws ResourceNotFoundException;
    void deleteExamProgrammingQuestions(AddDeleteExamProgrammingQuestionsRequest request, int examId) throws ResourceNotFoundException;


    //exam category details
    List<ExamCategoryDetails> getAllExamCategory();
    ExamCategoryDetails getExamCategoryById(int id) throws ResourceNotFoundException;
    List<ExamCategoryDetails> getExamCategoryByExamId(int examId) throws ResourceNotFoundException;
    void addExamCategory(AddUpdateExamCategoryDetailsRequest request) throws ResourceNotFoundException, InvalidValueException;
    void updateExamCategory(AddUpdateExamCategoryDetailsRequest request, int id) throws ResourceNotFoundException, InvalidValueException;
    void deleteExamCategory(int id) throws ResourceNotFoundException;




}
