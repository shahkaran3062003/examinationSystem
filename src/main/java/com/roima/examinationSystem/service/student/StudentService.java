package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.dto.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.CollegeRepository;
import com.roima.examinationSystem.repository.StudentRepository;
import com.roima.examinationSystem.repository.UserRepository;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class StudentService implements IStudentService{


    private final StudentRepository studentRepository;
    private final CollegeRepository collegeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
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
}
