package com.roima.examinationSystem.service.admin.studentManagement;


import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.dto.admin.*;
import com.roima.examinationSystem.dto.student.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import com.roima.examinationSystem.utils.FileManagementUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentManagementService implements IStudentManagementService {

    private final StudentRepository studentRepository;
    private final CollegeRepository collegeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final StudentExamDetailsRepository studentExamDetailsRepository;
    private final StudentMcqAnswerRepository studentMcqAnswerRepository;
    private final StudentProgrammingAnswerRepository studentProgrammingAnswerRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileManagementUtil fileManagementUtil;



    //---------------------------------Student---------------------------------------
    @Override
    public List<StudentDto> getAllStudents() {
        return getConvertedDtoList(studentRepository.findAll());
    }

    @Override
    public List<StudentDto> getStudentsByCollegeId(int collegeId) {
        return getConvertedDtoList(studentRepository.findAllByCollegeId(collegeId));
    }

    @Override
    public StudentDto getStudentById(int id) throws ResourceNotFoundException {
        return convertToDto(studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found!")));
    }

    @Override
    public StudentDto getStudentByEmail(String email) throws ResourceNotFoundException {
        return convertToDto(studentRepository.findByUserEmail(email).orElseThrow(()-> new ResourceNotFoundException("Student not found!")));
    }

    @Override
    public void addStudent(AddStudentRequest student) throws ResourceNotFoundException, ResourceExistsException {

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        User user = student.getUser();
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ResourceExistsException("User already exists!");
        }

        College college = collegeRepository.findByName(student.getCollege_name()).orElse(
            student.getCollege()
        );

        userRepository.save(user);
        collegeRepository.save(college);
        studentRepository.save(student.getStudent(user, college));
    }

    @Override
    public void updateStudent(UpdateStudentRequest request, int id) throws ResourceNotFoundException, ResourceExistsException {

        Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        College college = collegeRepository.findById(request.getCollege_id())
                .orElseThrow(()-> new ResourceNotFoundException("College not found!"));

        User oldUser = student.getUser();
        if(!(oldUser.getEmail().equals(request.getEmail())) && userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User already exists!");
        }

        oldUser.setFullName(request.getFullName());
        oldUser.setEmail(request.getEmail());
        oldUser.setPassword(passwordEncoder.encode(request.getPassword()));



//        student.setName(request.getName());
        student.setContact(request.getContact());
        student.setEnrollment_number(request.getEnrollment_number());
        student.setYear(request.getYear());
        student.setSemester(request.getSemester());
        student.setCgpa(request.getCgpa());
        student.setBacklog(request.getBacklog());
        student.setDepartment(request.getDepartment());
        student.setUser(oldUser);
        student.setCollege(college);

        userRepository.save(oldUser);
        studentRepository.save(student);

    }

    @Override
    public void deleteStudentById(int id) throws ResourceNotFoundException {

        Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found!"));
        studentRepository.delete(student);

    }

    public List<StudentDto> getConvertedDtoList(List<Student> students){
        return students.stream().map(this::convertToDto).toList();
    }


    public StudentDto convertToDto(Student student){
        return modelMapper.map(student, StudentDto.class);
    }



    //----------------------------------Student Exam Details--------------------------------------
    @Override
    public List<AdminStudentExamDetailsDto> getAllStudentExamDetailsByStudentId(int studentId) {
//        return studentExamDetailsRepository.findAllByStudentId(studentId);
        return convertToAdminStudentExamDetailsDtoList(studentExamDetailsRepository.findAllByStudentId(studentId));
    }

    @Override
    public AdminStudentExamDetailsDto getStudentExamDetailsByStudentIdAndExamId(int studentId, int examId) throws ResourceNotFoundException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(studentId,examId);

        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        return convertToDto(studentExamDetails);

    }

    @Override
    public List<AdminStudentExamDetailsDto> getAllStudentExamDetailsByExamId(int examId) {
//        return studentExamDetailsRepository.findExamResultsSorted(examId);
        return convertToAdminStudentExamDetailsDtoList(studentExamDetailsRepository.findExamResultsSorted(examId));
    }

    @Override
    public void deleteStudentExamDetailsById(int id) throws ResourceNotFoundException {

        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Exam Details not found!"));
        studentExamDetailsRepository.delete(studentExamDetails);
    }

    //-----------------------------------------Student MCQ Answer---------------------------------------

    @Override
    public List<AdminStudentMcqAnswerDto> getStudentMcqAnswerByStudentId(int studentExamDetails) throws ResourceNotFoundException {
        return convertToAdminStudentMcqAnswerDtoList(studentMcqAnswerRepository.findAllByStudentExamDetailsId(studentExamDetails));
    }

    @Override
    public AdminStudentMcqAnswerDto getStudentMcqAnswerById(int id) throws ResourceNotFoundException {
        return convertToDto(studentMcqAnswerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Mcq Answer not found!")));
    }

    @Override
    public void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException {
        StudentMcqAnswer studentMcqAnswer = studentMcqAnswerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Mcq Answer not found!"));
        studentMcqAnswerRepository.delete(studentMcqAnswer);
    }


    //---------------------------------Student Programming Answer---------------------------------------

    @Override
    public List<AdminStudentProgrammingAnswerDto> getAllProgrammingAnswerByStudentId(int studentExamDetailsId) throws ResourceNotFoundException {
        return convertToAdminStudentProgrammingAnswerDtoList(studentProgrammingAnswerRepository.findAllByStudentExamDetailsId(studentExamDetailsId));
    }

    @Override
    public AdminStudentProgrammingAnswerDto getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException {
        return convertToAdminDto(studentProgrammingAnswerRepository.findById(programmingAnswerId).orElseThrow(()-> new ResourceNotFoundException("Programming Answer not found!")));
    }

    @Override
    public void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException {
        StudentProgrammingAnswer programmingAnswer = studentProgrammingAnswerRepository.findById(programmingAnswerId).orElseThrow(()-> new ResourceNotFoundException("Programming Answer not found!"));
        studentProgrammingAnswerRepository.delete(programmingAnswer);
    }


    private McqOptionsDto convertToDto(McqOptionsDto mcqOptions){
        if(mcqOptions.getImage()!=null){
            mcqOptions.setImage(fileManagementUtil.getLiveImagePath(mcqOptions.getImage()));
        }
        return mcqOptions;
    }

    private AdminMcqQuestionDto convertToDto(McqQuestions mcqQuestions){
        AdminMcqQuestionDto dto = modelMapper.map(mcqQuestions,AdminMcqQuestionDto.class);
        if(dto.getImage()!=null){
            dto.setImage(fileManagementUtil.getLiveImagePath(dto.getImage()));
        }
        dto.setMcqOptions(getConvertedMcqOptionsDtoList(dto.getMcqOptions()));
        return dto;
    }

    private List<AdminMcqQuestionDto> getConvertedMcqQuestionsDtoList(List<McqQuestions> mcqQuestions){
        return mcqQuestions.stream().map(this::convertToDto).toList();
    }

    private List<McqOptionsDto> getConvertedMcqOptionsDtoList(List<McqOptionsDto> mcqOptionsDtos){
        return mcqOptionsDtos.stream().map(this::convertToDto).toList();
    }



    private AdminStudentMcqAnswerDto convertToDto(StudentMcqAnswer studentMcqAnswer){
        AdminStudentMcqAnswerDto adminStudentMcqAnswerDto = modelMapper.map(studentMcqAnswer,AdminStudentMcqAnswerDto.class);

        adminStudentMcqAnswerDto.setMcqQuestions(
                convertToDto(studentMcqAnswer.getMcqQuestions())
        );

        return adminStudentMcqAnswerDto;
    }

    private List<AdminStudentMcqAnswerDto> convertToAdminStudentMcqAnswerDtoList(List<StudentMcqAnswer> studentMcqAnswersList){
        return studentMcqAnswersList.stream().map(this::convertToDto).toList();
    }






    private AdminStudentProgrammingTestCaseAnswerDto convertToDto(StudentProgramTestCaseAnswer studentProgramTestCaseAnswer){
        return modelMapper.map(studentProgramTestCaseAnswer,AdminStudentProgrammingTestCaseAnswerDto.class);
    }

    private List<AdminStudentProgrammingTestCaseAnswerDto> convertToAdminStudentProgrammingTestCaseAnswerDtoList(List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswerList){
        return studentProgramTestCaseAnswerList.stream().map(this::convertToDto).toList();
    }


    private AdminStudentProgrammingAnswerDto convertToAdminDto(StudentProgrammingAnswer studentProgrammingAnswer){
        AdminStudentProgrammingAnswerDto adminStudentProgrammingAnswerDto = modelMapper.map(studentProgrammingAnswer, AdminStudentProgrammingAnswerDto.class);

        adminStudentProgrammingAnswerDto.setStudentProgramTestCaseAnswer(
                convertToAdminStudentProgrammingTestCaseAnswerDtoList(studentProgrammingAnswer.getStudentProgramTestCaseAnswer())
        );

        return adminStudentProgrammingAnswerDto;
    }

    private List<AdminStudentProgrammingAnswerDto> convertToAdminStudentProgrammingAnswerDtoList(List<StudentProgrammingAnswer> studentProgrammingAnswerList){
        return studentProgrammingAnswerList.stream().map(this::convertToAdminDto).toList();
    }


    private AdminStudentExamDetailsDto convertToDto(StudentExamDetails studentExamDetails){
        AdminStudentExamDetailsDto adminStudentExamDetailsDto =  modelMapper.map(studentExamDetails,AdminStudentExamDetailsDto.class);


        adminStudentExamDetailsDto.setStudentMcqAnswers(
                convertToAdminStudentMcqAnswerDtoList(studentExamDetails.getStudentMcqAnswers())
        );

        adminStudentExamDetailsDto.setStudentProgrammingAnswers(
                convertToAdminStudentProgrammingAnswerDtoList(studentExamDetails.getStudentProgrammingAnswers())
        );

        adminStudentExamDetailsDto.setExamMonitors(
                convertToAdminExamMonitorDtoList(studentExamDetails.getExamMonitors())
        );


        return adminStudentExamDetailsDto;

    }

    private List<AdminStudentExamDetailsDto> convertToAdminStudentExamDetailsDtoList(List<StudentExamDetails> studentExamDetailsList){
        return studentExamDetailsList.stream().map(this::convertToDto).toList();
    }

    private AdminExamMonitorDto convertToDto(ExamMonitor examMonitor){
        AdminExamMonitorDto adminExamMonitorDto = modelMapper.map(examMonitor,AdminExamMonitorDto.class);
        adminExamMonitorDto.setScreenImage(fileManagementUtil.getLiveImagePath(adminExamMonitorDto.getScreenImage()));
        adminExamMonitorDto.setUserImage(fileManagementUtil.getLiveImagePath(adminExamMonitorDto.getUserImage()));

        return adminExamMonitorDto;
    }

    private List<AdminExamMonitorDto> convertToAdminExamMonitorDtoList(List<ExamMonitor> examMonitorList){
        return examMonitorList.stream().map(this::convertToDto).toList();
    }

}
