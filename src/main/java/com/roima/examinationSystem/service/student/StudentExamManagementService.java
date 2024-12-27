package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.dto.student.StudentExamDetailsDto;
import com.roima.examinationSystem.dto.student.StudentExamDto;
import com.roima.examinationSystem.dto.student.StudentMcqQuestionDto;
import com.roima.examinationSystem.exception.ExamException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.StartExamRequest;
import com.roima.examinationSystem.request.SubmitExamRequest;
import com.roima.examinationSystem.utils.FileManagementUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final ExamMonitorRepository examMonitorRepository;
    private final LanguageRepository languageRepository;
    private final ModelMapper modelMapper;
    private final HttpServletRequest httpServletRequest;
    private final FileManagementUtil fileManagementUtil;


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

            String examIp = httpServletRequest.getRemoteAddr();

            studentExamDetails = new StudentExamDetails(
                    examIp,
                    true,
                    totalMcqQuestions,
                    totalUnattemptedMcqQuestions,
                    totalProgrammingQuestions,
                    totalUnattemptedProgrammingQuestions,
                    student,
                    exam
            );
            studentExamDetailsRepository.save(studentExamDetails);
        }else if(studentExamDetails.getIsSubmitted()){
                throw new ExamException("Exam already submitted!");
        }
        return convertToExamDto(exam);
    }


    public void submitExam(SubmitExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());

        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        if(!studentExamDetails.getIsStarted()){
            throw new ExamException("Exam not started!");
        }

        if(studentExamDetails.getIsSubmitted()){
            throw new ExamException("Exam already submitted!");
        }

        if(!studentExamDetails.getExamIp().equals(httpServletRequest.getRemoteAddr())){
            throw new ExamException("Different IP address!");
        }

        studentExamDetails.setIsSubmitted(true);
        studentExamDetailsRepository.save(studentExamDetails);
    }

    @Override
    public void submitMcqQuestion(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(request.getStudentId(), request.getExamId());
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        if(studentExamDetails==null || !studentExamDetails.getIsStarted()){
            throw new ResourceNotFoundException("Student not started exam yet!");
        }

        if(studentExamDetails.getIsSubmitted()){
            throw new ExamException("Exam already submitted!");
        }

        if(!isExamStarted(exam.getStart_datetime())){
            throw new ExamException("Exam not started yet!");
        }

        if(isExamEnded(exam.getEnd_datetime())){
            throw new ExamException("Exam ended!");
        }

        if(!studentExamDetails.getExamIp().equals(httpServletRequest.getRemoteAddr())){
            throw new ExamException("Different IP address!");
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

        if(studentExamDetails==null || !studentExamDetails.getIsStarted()){
            throw new ResourceNotFoundException("Student not started exam yet!");
        }

        if(studentExamDetails.getIsSubmitted()){
            throw new ResourceExistsException("Exam already submitted!");
        }

        if(!isExamStarted(exam.getStart_datetime())){
            throw new ExamException("Exam not started yet!");
        }

        if(isExamEnded(exam.getEnd_datetime())){
            throw new ExamException("Exam ended!");
        }

        if(!studentExamDetails.getExamIp().equals(httpServletRequest.getRemoteAddr())){
            throw new ExamException("Different IP address!");
        }


        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));
        Language language = languageRepository.findById(request.getLanguageId()).orElseThrow(()-> new ResourceNotFoundException("Language not found!"));


        StudentProgrammingAnswer studentProgrammingAnswer = studentProgrammingAnswerRepository.findByExamIdAndStudentIdAndProgrammingQuestionsId(request.getExamId(),request.getStudentId(),request.getProgrammingQuestionsId());

        boolean isNewAnswer = false;
        if(studentProgrammingAnswer == null) {
            studentProgrammingAnswer = new StudentProgrammingAnswer(
                    request.getSubmittedCode(),
                    exam,
                    student,
                    programmingQuestions,
                    language
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

    @Override
    public void monitorExam(int studentId, int examId, MultipartFile screenImage, MultipartFile userImage) throws ResourceNotFoundException, InvalidValueException, IOException {
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));

        ExamMonitor examMonitor = new ExamMonitor(exam, student, fileManagementUtil.saveImage(screenImage), fileManagementUtil.saveImage(userImage));

        examMonitorRepository.save(examMonitor);
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
            dto.setImage(fileManagementUtil.getLiveImagePath(dto.getImage()));
        }
        dto.setMcqOptions(getConvertedMcqOptionsDtoList(dto.getMcqOptions()));
        return dto;
    }

    private McqOptionsDto convertToDto(McqOptionsDto mcqOptions){
        if(mcqOptions.getImage()!=null){
            mcqOptions.setImage(fileManagementUtil.getLiveImagePath(mcqOptions.getImage()));
        }
        return mcqOptions;
    }


    private boolean isExamEnded(LocalDateTime endDatetime) {
        return LocalDateTime.now().isAfter(endDatetime);
    }

    private boolean isExamStarted(LocalDateTime startDatetime) {
        return LocalDateTime.now().isAfter(startDatetime);
    }

}
