package com.roima.examinationSystem.service.studentProgrammingAnswer;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentProgrammingAnswerService implements IStudentProgrammingAnswerService {

    private final StudentProgrammingAnswerRepository studentProgrammingAnswerRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final StudentExamDetailsRepository studentExamDetailsRepository;

    @Override
    public void addStudentProgrammingAnswer(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException {

        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));

//        TODO compile and run all testcase of programming question and update testcase result
//        TODO handle compile error and runtime error

        StudentProgrammingAnswer studentProgrammingAnswer = studentProgrammingAnswerRepository.findByExamIdAndStudentIdAndProgrammingQuestionsId(request.getExamId(),request.getStudentId(),request.getProgrammingQuestionsId());

        boolean isNewAnswer = false;
        if(studentProgrammingAnswer == null) {
            studentProgrammingAnswer = new StudentProgrammingAnswer(
                    request.getSubmittedCode(),
                    exam,
                    student,
                    programmingQuestions
            );
            isNewAnswer = true;
        }else{
            studentProgrammingAnswer.setSubmittedCode(request.getSubmittedCode());
        }
        studentProgrammingAnswerRepository.save(studentProgrammingAnswer);

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentAndExam(student,exam);

        if(studentExamDetails==null){
            int totalProgrammingQuestions = exam.getProgrammingQuestions().size();
            int totalUnattemptedProgrammingQuestions = totalProgrammingQuestions;
            studentExamDetails = new StudentExamDetails(
                    totalProgrammingQuestions,
                    totalUnattemptedProgrammingQuestions,
                    student,
                    exam
            );
            studentExamDetailsRepository.save(studentExamDetails);
        }

//        TODO check if student program pass all testcase or not if yes then update totalSolvedProgrammingQuestions of studentExamDetails else update totalUnsolvedProgrammingQuestions

        if(isNewAnswer) {
            studentExamDetails.setTotalUnattemptedProgrammingQuestions(studentExamDetails.getTotalUnattemptedProgrammingQuestions() - 1);
        }
        studentExamDetailsRepository.save(studentExamDetails);

    }

    @Override
    public List<StudentProgrammingAnswer> getAllProgrammingAnswerByStudentId(int studentId) throws ResourceNotFoundException {
        return studentProgrammingAnswerRepository.findAllByStudentId(studentId);
    }

    @Override
    public StudentProgrammingAnswer getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException {
        return studentProgrammingAnswerRepository.findById(programmingAnswerId).orElseThrow(()-> new ResourceNotFoundException("Programming Answer not found!"));
    }

    @Override
    public void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException {
        StudentProgrammingAnswer programmingAnswer = studentProgrammingAnswerRepository.findById(programmingAnswerId).orElseThrow(()-> new ResourceNotFoundException("Programming Answer not found!"));
        studentProgrammingAnswerRepository.delete(programmingAnswer);
    }
}
