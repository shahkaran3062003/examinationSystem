package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.dto.student.*;
import com.roima.examinationSystem.exception.*;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.StartExamRequest;
import com.roima.examinationSystem.request.SubmitExamRequest;
import com.roima.examinationSystem.utils.FileManagementUtil;
import com.roima.examinationSystem.utils.judge0.BatchData;
import com.roima.examinationSystem.utils.judge0.BatchSubmissionResult;
import com.roima.examinationSystem.utils.judge0.BatchTestCase;
import com.roima.examinationSystem.utils.judge0.Judge0Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final Judge0Util judge0Util;


    @Override
    public List<StudentExamDetailsDto> getExamByCollegeId(int collegeId) {
        List<Exam> exams = examRepository.findAllByCollegeId(collegeId);
        return getConvertedDtoList(exams);
    }

    @Override
    public Map<String,Object> startExam(StartExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException {

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
        Map<String,Object> examDetails = new HashMap<>();
        Map<String,Object> submittedData = new HashMap<>();

        submittedData.put("MCQ",getConvertedStudentSubmittedMcqAnswer(
                studentExamDetails.getStudentMcqAnswers()!=null?studentExamDetails.getStudentMcqAnswers():new ArrayList<>()
        ));
        submittedData.put("PROGRAMMING",getConvertedStudentProgrammingQuestions(
                studentExamDetails.getStudentProgrammingAnswers()!=null?studentExamDetails.getStudentProgrammingAnswers():new ArrayList<>()
        ));

        examDetails.put("exam", convertToExamDto(exam));
        examDetails.put("submitted", submittedData);

        System.out.println(examDetails);
        return examDetails;
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

        McqQuestions mcqQuestions = mcqQuestionsRepository.findById(request.getMcqQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));

        boolean isCorrect = request.getSelectedOption()==mcqQuestions.getCorrect_option();

        StudentMcqAnswer studentMcqAnswer = studentMcqAnswerRepository.findByStudentExamDetailsIdAndMcqQuestionsId(studentExamDetails.getId(), request.getMcqQuestionsId());

        boolean isNewAnswer = false;
        if(studentMcqAnswer == null) {
            studentMcqAnswer = new StudentMcqAnswer(request.getSelectedOption(), studentExamDetails, mcqQuestions);
            isNewAnswer = true;
        }else{
            studentMcqAnswer.setSelectedOption(request.getSelectedOption());
        }

        if(isNewAnswer) {
            studentExamDetails.setTotalUnattemptedMcqQuestions(studentExamDetails.getTotalUnattemptedMcqQuestions() - 1);
            if(isCorrect){
                studentExamDetails.setTotalCorrectMcqAnswers(studentExamDetails.getTotalCorrectMcqAnswers()+1);
            }else{
                studentExamDetails.setTotalWrongMcqAnswers(studentExamDetails.getTotalWrongMcqAnswers()+1);
            }
        }else{
            if(isCorrect && !studentMcqAnswer.isCorrect()){
                studentExamDetails.setTotalCorrectMcqAnswers(studentExamDetails.getTotalCorrectMcqAnswers()+1);
                studentExamDetails.setTotalWrongMcqAnswers(studentExamDetails.getTotalWrongMcqAnswers()-1);
            }else if(!isCorrect && studentMcqAnswer.isCorrect()){
                studentExamDetails.setTotalCorrectMcqAnswers(studentExamDetails.getTotalCorrectMcqAnswers()-1);
                studentExamDetails.setTotalWrongMcqAnswers(studentExamDetails.getTotalWrongMcqAnswers()+1);
            }
        }

        studentMcqAnswer.setCorrect(isCorrect);
        studentMcqAnswerRepository.save(studentMcqAnswer);
        studentExamDetailsRepository.save(studentExamDetails);

    }

    @Override
    @Transactional
    public void submitProgrammingQuestion(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException, InvalidValueException, FetchException {

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

        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionsId()).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));
        Language language = languageRepository.findById(request.getLanguageId()).orElseThrow(()-> new ResourceNotFoundException("Language not found!"));


        StudentProgrammingAnswer studentProgrammingAnswer = studentProgrammingAnswerRepository.findByStudentExamDetailsIdAndProgrammingQuestionsId(studentExamDetails.getId(),request.getProgrammingQuestionsId());

        boolean isNewAnswer = false;
        if(studentProgrammingAnswer == null) {
            studentProgrammingAnswer = new StudentProgrammingAnswer(
                    request.getSubmittedCode(),
                    studentExamDetails,
                    programmingQuestions,
                    language
            );
            isNewAnswer = true;
        }else{
            studentProgrammingAnswer.setSubmittedCode(request.getSubmittedCode());
        }
        studentProgrammingAnswer.setLanguage(language);
        studentProgrammingAnswerRepository.save(studentProgrammingAnswer);


        BatchData batchData = new BatchData();
        batchData.setJudge0LanguageId(language.getJudge0Id());
        batchData.setCode(request.getSubmittedCode());

        List<ProgrammingTestCase> programmingTestCaseList = programmingQuestions.getProgrammingTestCase();
        List<StudentProgramTestCaseAnswer> studentProgrammingTestCaseAnswerList = studentProgrammingAnswer.getStudentProgramTestCaseAnswer();
        List<BatchTestCase> testCaseList = new ArrayList<>();

        for(ProgrammingTestCase testCase: programmingTestCaseList){
            BatchTestCase batchTestCase = new BatchTestCase(testCase.getInput(),testCase.getOutput());
            testCaseList.add(batchTestCase);
        }

        batchData.setTestCaseList(testCaseList);
        List<BatchSubmissionResult> batchSubmissionResultList = judge0Util.batchSubmissions(batchData);

        int totalPassTestCase = 0;
        int totalFailedTestCase = 0;
        boolean isSolved = true;

        for(int i=0;i<programmingTestCaseList.size();i++){
            if(batchSubmissionResultList.get(i).getStatus().getId()==3){
                totalPassTestCase++;

                if(isNewAnswer){
                    System.out.println("in true's isNew");

                    if(studentProgrammingTestCaseAnswerList == null){
                        studentProgrammingTestCaseAnswerList = new ArrayList<>();
                    }

                    studentProgrammingTestCaseAnswerList.add(new StudentProgramTestCaseAnswer(true,programmingTestCaseList.get(i),studentProgrammingAnswer));
                }
                else{
                    System.out.println("in true's not isNew");
                    studentProgrammingTestCaseAnswerList.get(i).setStatus(true);
                }
            }else{
                isSolved = false;
                totalFailedTestCase++;
                if(isNewAnswer){
                    System.out.println("in false's isNew");
                    if(studentProgrammingTestCaseAnswerList == null){
                        studentProgrammingTestCaseAnswerList = new ArrayList<>();
                    }
                    studentProgrammingTestCaseAnswerList.add(new StudentProgramTestCaseAnswer(false,programmingTestCaseList.get(i), studentProgrammingAnswer));
                }else{
                    System.out.println("in false's not isNew");
                    studentProgrammingTestCaseAnswerList.get(i).setStatus(false);
                }
            }
        }

        int totalSolvedProgrammingQuestions = studentExamDetails.getTotalSolvedProgrammingQuestions();
        int totalUnsolvedProgrammingQuestions = studentExamDetails.getTotalUnsolvedProgrammingQuestions();
        int totalUnattemptedProgrammingQuestions = studentExamDetails.getTotalUnattemptedProgrammingQuestions();

        if(isNewAnswer) {
            studentExamDetails.setTotalUnattemptedProgrammingQuestions(totalUnattemptedProgrammingQuestions - 1);

            if(isSolved){
                studentExamDetails.setTotalSolvedProgrammingQuestions(totalSolvedProgrammingQuestions+1);
            }else{
                studentExamDetails.setTotalUnsolvedProgrammingQuestions(totalUnsolvedProgrammingQuestions+1);
            }
        }else{
            if(isSolved && !studentProgrammingAnswer.getIsSolved()){
                studentExamDetails.setTotalSolvedProgrammingQuestions(totalSolvedProgrammingQuestions+1);
                studentExamDetails.setTotalUnsolvedProgrammingQuestions(totalUnsolvedProgrammingQuestions-1);
            }else if (!isSolved && studentProgrammingAnswer.getIsSolved()){
                studentExamDetails.setTotalSolvedProgrammingQuestions(totalSolvedProgrammingQuestions-1);
                studentExamDetails.setTotalUnsolvedProgrammingQuestions(totalUnsolvedProgrammingQuestions+1);
            }
        }


        studentProgrammingAnswer.setStudentProgramTestCaseAnswer(studentProgrammingTestCaseAnswerList);

        studentProgrammingAnswer.setTotalPassTestCount(totalPassTestCase);
        studentProgrammingAnswer.setTotalFailTestCount(totalFailedTestCase);
        studentProgrammingAnswer.setIsSolved(isSolved);

        studentProgrammingAnswerRepository.save(studentProgrammingAnswer);
        studentExamDetailsRepository.save(studentExamDetails);

    }

    @Override
    public void monitorExam(int studentId, int examId, MultipartFile screenImage, MultipartFile userImage) throws ResourceNotFoundException, InvalidValueException, IOException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(studentId,examId);
        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        ExamMonitor examMonitor = new ExamMonitor(studentExamDetails, fileManagementUtil.saveImage(screenImage), fileManagementUtil.saveImage(userImage));

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

    private List<StudentSubmittedMcqQuestionDto> getConvertedStudentSubmittedMcqAnswer(List<StudentMcqAnswer> studentMcqAnswers){
        return studentMcqAnswers.stream().map(this::convertToDto).toList();
    }

    private StudentSubmittedMcqQuestionDto convertToDto(StudentMcqAnswer studentMcqAnswer){
        StudentSubmittedMcqQuestionDto dto = new StudentSubmittedMcqQuestionDto();
        dto.setId(studentMcqAnswer.getMcqQuestions().getId());
        dto.setSelectedOption(studentMcqAnswer.getSelectedOption());
        return dto;
    }

    private List<StudentSubmittedProgrammingQuestionDto> getConvertedStudentProgrammingQuestions(List<StudentProgrammingAnswer> studentProgrammingAnswers) {
        return studentProgrammingAnswers.stream().map(this::convertToDto).toList();
    }

    private StudentSubmittedProgrammingQuestionDto convertToDto(StudentProgrammingAnswer studentProgrammingAnswer)
    {
        StudentSubmittedProgrammingQuestionDto dto = new StudentSubmittedProgrammingQuestionDto();
        dto.setId(studentProgrammingAnswer.getProgrammingQuestions().getId());
        dto.setSubmittedCode(studentProgrammingAnswer.getSubmittedCode());
        dto.setLanguageId(studentProgrammingAnswer.getLanguage().getId());
        return dto;
    }


    private boolean isExamEnded(LocalDateTime endDatetime) {
        return LocalDateTime.now().isAfter(endDatetime);
    }

    private boolean isExamStarted(LocalDateTime startDatetime) {
        return LocalDateTime.now().isAfter(startDatetime);
    }

}
