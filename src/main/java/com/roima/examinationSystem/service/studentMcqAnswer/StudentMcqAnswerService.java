package com.roima.examinationSystem.service.studentMcqAnswer;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentMcqAnswerService implements IStudentMcqAnswerService {

    private final StudentMcqAnswerRepository studentMcqAnswerRepository;
    private final ExamRepository examRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final StudentRepository studentRepository;
    private final StudentExamDetailsRepository studentExamDetailsRepository;

    @Override
    public void addStudentMcqAnswer(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        McqQuestions mcqQuestions = mcqQuestionsRepository.findById(request.getMcqQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));

        boolean isCorrect = request.getSelectedOption()==mcqQuestions.getCorrect_option();

        StudentMcqAnswer studentMcqAnswer = studentMcqAnswerRepository.findByExamIdAndStudentIdAndMcqQuestionsId(request.getExamId(), request.getStudentId(), request.getMcqQuestionsId());

        boolean isNewAnswer = false;
        if(studentMcqAnswer == null) {
            studentMcqAnswer = new StudentMcqAnswer(request.getSelectedOption(), exam, student, mcqQuestions);
            studentMcqAnswer.setCorrect(isCorrect);
            isNewAnswer = true;
        }else{
            studentMcqAnswer.setSelectedOption(request.getSelectedOption());
            studentMcqAnswer.setCorrect(isCorrect);
        }

        studentMcqAnswerRepository.save(studentMcqAnswer);

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentAndExam(student,exam);

        if(studentExamDetails==null){

            int totalMcqQuestions = exam.getMcqQuestions().size();
            int totalProgrammingQuestions = exam.getProgrammingQuestions().size();
            int totalUnattemptedMcqQuestions = totalMcqQuestions;
            int totalUnattemptedProgrammingQuestions = totalProgrammingQuestions;

            studentExamDetails = new StudentExamDetails(
                    totalMcqQuestions,
                    totalUnattemptedMcqQuestions,
                    totalProgrammingQuestions,
                    totalUnattemptedProgrammingQuestions,
                    student,
                    exam
            );
            studentExamDetailsRepository.save(studentExamDetails);
        }

        if(isCorrect){
            studentExamDetails.setTotalCorrectMcqAnswers(studentExamDetails.getTotalCorrectMcqAnswers()+1);
        }else{
            studentExamDetails.setTotalWrongMcqAnswers(studentExamDetails.getTotalWrongMcqAnswers()+1);
        }

        if(isNewAnswer) {
            studentExamDetails.setTotalUnattemptedMcqQuestions(studentExamDetails.getTotalUnattemptedMcqQuestions() - 1);
        }
        studentExamDetailsRepository.save(studentExamDetails);

    }

    @Override
    public List<StudentMcqAnswer> getStudentMcqAnswerByStudentId(int studentId) throws ResourceNotFoundException {
        return studentMcqAnswerRepository.findAllByStudentId(studentId);
    }

    @Override
    public StudentMcqAnswer getStudentMcqAnswerById(int id) throws ResourceNotFoundException {
        return studentMcqAnswerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Mcq Answer not found!"));
    }

    @Override
    public void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException {
        StudentMcqAnswer studentMcqAnswer = studentMcqAnswerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Mcq Answer not found!"));
        studentMcqAnswerRepository.delete(studentMcqAnswer);
    }
}
