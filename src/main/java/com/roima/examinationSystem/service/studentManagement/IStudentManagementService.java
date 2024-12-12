package com.roima.examinationSystem.service.studentManagement;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.StudentExamDetails;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import com.roima.examinationSystem.model.StudentProgrammingAnswer;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;

import java.util.List;

public interface IStudentManagementService {

    //Student
    List<Student> getAllStudents();
    List<Student> getStudentsByCollegeId(int id);
    Student getStudentById(int id) throws ResourceNotFoundException;
    void addStudent(AddStudentRequest student) throws ResourceNotFoundException, ResourceExistsException;
    void updateStudent(UpdateStudentRequest request, int id) throws ResourceNotFoundException, ResourceExistsException;
    void deleteStudentById(int id) throws ResourceNotFoundException;
    Student getStudentByEmail(String email) throws ResourceNotFoundException;


    //Student Exam Details
    List<StudentExamDetails> getAllStudentExamDetailsByStudentId(int studentId);
    StudentExamDetails getStudentExamDetailsByStudentIdAndExamId(int studentId,int examId) throws ResourceNotFoundException;
    List<StudentExamDetails> getAllStudentExamDetailsByExamId(int examId);

    //Student MCQ Answer
    void addStudentMcqAnswer(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException;
    List<StudentMcqAnswer> getStudentMcqAnswerByStudentId(int studentId) throws ResourceNotFoundException;
    StudentMcqAnswer getStudentMcqAnswerById(int id) throws ResourceNotFoundException;
    void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException;

    //Student Programming Answer
    void addStudentProgrammingAnswer(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException;
    List<StudentProgrammingAnswer> getAllProgrammingAnswerByStudentId(int studentId) throws ResourceNotFoundException;
    StudentProgrammingAnswer getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;
    void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;




}