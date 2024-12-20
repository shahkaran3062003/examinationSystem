package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.dto.admin.AdminMcqQuestionDto;
import com.roima.examinationSystem.dto.student.StudentExamDetailsDto;
import com.roima.examinationSystem.dto.student.StudentExamDto;
import com.roima.examinationSystem.dto.student.StudentMcqQuestionDto;
import com.roima.examinationSystem.exception.ExamException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.StartExamRequest;
import com.roima.examinationSystem.request.SubmitExamRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentExamManagementService implements IStudentExamManagementService {

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final StudentExamDetailsRepository studentExamDetailsRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final StudentMcqAnswerRepository studentMcqAnswerRepository;
    private final StudentProgrammingAnswerRepository studentProgrammingAnswerRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentExamDetailsDto> getExamByCollegeId(int collegeId) {
        List<Exam> exams = examRepository.findAllByCollegeId(collegeId);
        return getConvertedDtoList(exams);
    }

    @Override
    public StudentExamDto startExam(StartExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {

        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));


        if(!isExamStarted(exam.getStart_datetime())){
            throw new ExamException("Exam not started yet!");
        }

        if(isExamEnded(exam.getEnd_datetime())){
            throw new ExamException("Exam ended!");
        }

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());

        if(studentExamDetails==null){

            int totalMcqQuestions = exam.getMcqQuestions().size();
            int totalProgrammingQuestions = exam.getProgrammingQuestions().size();
            int totalUnattemptedMcqQuestions = totalMcqQuestions;
            int totalUnattemptedProgrammingQuestions = totalProgrammingQuestions;

            studentExamDetails = new StudentExamDetails(
                    true,
                    totalMcqQuestions,
                    totalUnattemptedMcqQuestions,
                    totalProgrammingQuestions,
                    totalUnattemptedProgrammingQuestions,
                    student,
                    exam
            );
            studentExamDetailsRepository.save(studentExamDetails);
        }else if(studentExamDetails.isSubmitted()){
                throw new ExamException("Exam already submitted!");
        }

        System.out.println(exam.getMcqQuestions());


        return convertToExamDto(exam);
    }


    public void submitExam(SubmitExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());

        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        if(!studentExamDetails.isStarted()){
            throw new ExamException("Exam not started!");
        }

        if(studentExamDetails.isSubmitted()){
            throw new ExamException("Exam already submitted!");
        }
        studentExamDetails.setSubmitted(true);
        studentExamDetailsRepository.save(studentExamDetails);
    }

    @Override
    public void submitMcqQuestion(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        if(studentExamDetails==null || !studentExamDetails.isStarted()){
            throw new ResourceNotFoundException("Student not started exam yet!");
        }

        if(studentExamDetails.isSubmitted()){
            throw new ExamException("Exam already submitted!");
        }

        if(!isExamStarted(exam.getStart_datetime())){
            throw new ExamException("Exam not started yet!");
        }

        if(isExamEnded(exam.getEnd_datetime())){
            throw new ExamException("Exam ended!");
        }


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
    public void submitProgrammingQuestion(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));

        if(studentExamDetails==null || !studentExamDetails.isStarted()){
            throw new ResourceNotFoundException("Student not started exam yet!");
        }

        if(studentExamDetails.isSubmitted()){
            throw new ResourceExistsException("Exam already submitted!");
        }

        if(!isExamStarted(exam.getStart_datetime())){
            throw new ExamException("Exam not started yet!");
        }

        if(isExamEnded(exam.getEnd_datetime())){
            throw new ExamException("Exam ended!");
        }


        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));

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

//        TODO check if student program pass all testcase or not if yes then update totalSolvedProgrammingQuestions of studentExamDetails else update totalUnsolvedProgrammingQuestions

        if(isNewAnswer) {
            studentExamDetails.setTotalUnattemptedProgrammingQuestions(studentExamDetails.getTotalUnattemptedProgrammingQuestions() - 1);
        }
        studentExamDetailsRepository.save(studentExamDetails);

    }


    private StudentExamDetailsDto convertToExamDetailsDto(Exam exam) {
        return modelMapper.map(exam, StudentExamDetailsDto.class);
    }

    private StudentExamDto convertToExamDto(Exam exam) {
        StudentExamDto dto = modelMapper.map(exam, StudentExamDto.class);
        dto.setMcqQuestions(getConvertedMcqQuestionsDtoList(exam.getMcqQuestions()));
        return dto;
    }

    private List<StudentExamDetailsDto> getConvertedDtoList(List<Exam> exams) {
        return exams.stream().map(this::convertToExamDetailsDto).toList();
    }

    private List<StudentMcqQuestionDto> getConvertedMcqQuestionsDtoList(List<McqQuestions> mcqQuestions){
        return mcqQuestions.stream().map(this::convertToDto).toList();
    }

    private List<McqOptionsDto> getConvertedMcqOptionsDtoList(List<McqOptionsDto> mcqOptionsDtos){
        return mcqOptionsDtos.stream().map(this::convertToDto).toList();
    }



    private StudentMcqQuestionDto convertToDto(McqQuestions mcqQuestions){
        StudentMcqQuestionDto dto = modelMapper.map(mcqQuestions, StudentMcqQuestionDto.class);
        if(dto.getImage()!=null){
            dto.setImage(getLiveImagePath(dto.getImage()));
        }
        dto.setMcqOptions(getConvertedMcqOptionsDtoList(dto.getMcqOptions()));
        return dto;
    }

    private McqOptionsDto convertToDto(McqOptionsDto mcqOptions){
        if(mcqOptions.getImage()!=null){
            mcqOptions.setImage(getLiveImagePath(mcqOptions.getImage()));
        }
        return mcqOptions;
    }

    private String getLiveImagePath(String image){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(image).toUriString();
    }

    private boolean isExamEnded(Date endDatetime) {
        return new Date().after(endDatetime);
    }

    private boolean isExamStarted(Date startDatetime) {
        return new Date().after(startDatetime);
    }

}
