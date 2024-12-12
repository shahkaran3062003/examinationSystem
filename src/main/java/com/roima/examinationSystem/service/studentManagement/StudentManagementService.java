package com.roima.examinationSystem.service.studentManagement;


import com.roima.examinationSystem.dto.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ExamRepository examRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final StudentProgrammingAnswerRepository studentProgrammingAnswerRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;



    //---------------------------------Student---------------------------------------
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByCollegeId(int collegeId) {
        return studentRepository.findAllByCollegeId(collegeId);
    }

    @Override
    public Student getStudentById(int id) throws ResourceNotFoundException {
        return studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found!"));
    }

    @Override
    public Student getStudentByEmail(String email) throws ResourceNotFoundException {
        Student student =  studentRepository.findByUserEmail(email);

        if(student == null)
        {
            throw new ResourceNotFoundException("Student not found!");
        }
        return student;
    }

    @Override
    public void addStudent(AddStudentRequest student) throws ResourceNotFoundException, ResourceExistsException {
        User user = student.getUser();
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ResourceExistsException("User already exists!");
        }


        College college = collegeRepository.findById(student.getCollege_id())
                .orElseThrow(()-> new ResourceNotFoundException("College not found!"));
        userRepository.save(user);
        studentRepository.save(student.getStudent(user, college));
    }

    @Override
    public void updateStudent(UpdateStudentRequest request, int id) throws ResourceNotFoundException, ResourceExistsException {

        Student student = getStudentById(id);
        College college = collegeRepository.findById(request.getCollege_id())
                .orElseThrow(()-> new ResourceNotFoundException("College not found!"));

        User oldUser = student.getUser();
        if(!(oldUser.getEmail().equals(request.getEmail())) && userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User already exists!");
        }

        oldUser.setUsername(request.getUsername());
        oldUser.setEmail(request.getEmail());
        oldUser.setPassword(request.getPassword());



        student.setName(request.getName());
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

        Student student = getStudentById(id);
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
    public List<StudentExamDetails> getAllStudentExamDetailsByStudentId(int studentId) {
        return studentExamDetailsRepository.findAllByStudentId(studentId);
    }

    @Override
    public StudentExamDetails getStudentExamDetailsByStudentIdAndExamId(int studentId, int examId) throws ResourceNotFoundException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(studentId,examId);

        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        return studentExamDetails;

    }

    @Override
    public List<StudentExamDetails> getAllStudentExamDetailsByExamId(int examId) {
        return studentExamDetailsRepository.findAllByExamId(examId);
    }



    //-------------------------------Student Mcq answer----------------------------------
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


    //---------------------------------Student Programming Answer---------------------------------------
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
