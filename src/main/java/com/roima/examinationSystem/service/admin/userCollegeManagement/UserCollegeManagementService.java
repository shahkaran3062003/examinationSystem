package com.roima.examinationSystem.service.admin.userCollegeManagement;


import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.dto.UserDto;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.LoginLog;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.CollegeRepository;
import com.roima.examinationSystem.repository.LoginLogRepository;
import com.roima.examinationSystem.repository.UserRepository;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserCollegeManagementService implements IUserCollegeManagementService {

    private final UserRepository userRepository;
    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;
    private final LoginLogRepository loginLogRepository;

    //-----------------------------------User----------------------------------------------
    @Override
    public List<UserDto> getAllUsers() {

        return convertToUserDtoList(userRepository.findAll().stream().toList());
    }

    @Override
    public void addUser(AddUserRequest request) throws InvalidValueException, ResourceExistsException {

        try {
            boolean isUserPresent = userRepository.existsByEmail(request.getEmail());
            if (isUserPresent) {
                throw new ResourceExistsException("User already exists!");
            }
            User newUser = new User(
                    request.getFullName(),
                    request.getEmail(),
                    request.getPassword(),
                    Role.valueOf(request.getRole()));
            userRepository.save(newUser);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid user role!");
        }

    }

    @Override
    public void updateUser(UpdateUserRequest request, int userId) throws InvalidValueException, ResourceNotFoundException {


        try {
            User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found!"));

            Role role = Role.valueOf(request.getRole());

            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setPassword(request.getPassword());
            user.setRole(role);
            userRepository.save(user);
        } catch(IllegalArgumentException e){
            throw new InvalidValueException("Invalid user role!");
        }

    }

    @Override
    public void deleteUserById(int id) throws ResourceNotFoundException {

        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found!"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(int id) throws ResourceNotFoundException {
        return convertToDto(userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found!")));
    }

    @Override
    public UserDto getUserByEmail(String email) throws ResourceNotFoundException {
        return convertToDto( userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found!")));

    }

    @Override
    public List<UserDto> getUsersByRole(String role) throws InvalidValueException {
        try {
            Role erole = Role.valueOf(role);
            return convertToUserDtoList( userRepository.findByRole(erole));
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid user role!");
        }
    }


    //----------------------------------------College------------------------------

    @Override
    public List<CollegeDto> getAllColleges() {

        return convertToCollegeDtoList(collegeRepository.findAll());
    }

    @Override
    public CollegeDto getCollegeById(int id) throws ResourceNotFoundException {
        return convertToDto( collegeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("College not found!")));
    }

    @Override
    public CollegeDto getCollegeByName(String name) throws ResourceNotFoundException {

        return convertToDto(collegeRepository.findByName(name).orElseThrow(()->new ResourceNotFoundException("College not found!")));
    }

    @Override
    public void addCollege(AddCollegeRequest request) throws ResourceExistsException {

        boolean isPresentedName = collegeRepository.existsByName(request.getName());
        boolean isPresentedEmail = collegeRepository.existsByEmail(request.getEmail());
        if(isPresentedName || isPresentedEmail){
            throw new ResourceExistsException("College already exists!");
        }
        College newCollege;
        if(!(request.getName() == null) && !(request.getAddress()==null) && !(request.getEmail()==null)){
            newCollege = new College(request.getName(),request.getEmail(),request.getAddress());
        }else {
            newCollege = new College(request.getName(),request.getEmail());
        }
/*            else{
                throw new InvalidParametersException("Invalid parameters!!!");
            }*/
        collegeRepository.save(newCollege);

    }

    @Override
    public void updateCollege(UpdateCollegeRequest request, int id) throws ResourceExistsException, ResourceNotFoundException {
        College oldCollege = collegeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("College not found!"));

        if(!Objects.equals(oldCollege.getName(), request.getName()) && collegeRepository.existsByName(request.getName())){
            throw new ResourceExistsException("New College name already exists!");
        }

        if(!Objects.equals(oldCollege.getEmail(), request.getEmail()) && collegeRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("New College email already exists!");
        }

        if(!(request.getName()==null) && !(request.getAddress()==null) && !(request.getEmail()==null)){
            oldCollege.setName(request.getName());
            oldCollege.setAddress(request.getAddress());
            oldCollege.setEmail(request.getEmail());
        }else {
            oldCollege.setName(request.getName());
            oldCollege.setEmail(request.getEmail());
        }
        /*else{
            throw new InvalidParametersException("Invalid parameters!!!");
        }*/
        collegeRepository.save(oldCollege);

    }

    @Override
    public void deleteCollegeById(int id) throws ResourceNotFoundException {
        College college = collegeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("College not found!"));
        collegeRepository.delete(college);
    }



//    ----------------------------------------LoginLog----------------------------------------------


    @Override
    public List<LoginLog> getAllLoginLogs() {
        return loginLogRepository.findAll();
    }

    @Override
    public List<LoginLog> getLoginLogsByUserId(int userId) {
        return loginLogRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteLoginLogsByUserId(int userId) throws ResourceNotFoundException {

        if(userId != -1 && !userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found!");
        }

        List<LoginLog> logs = loginLogRepository.findAllByUserId(userId);
        loginLogRepository.deleteAll(logs);

    }

    @Override
    public void deleteLoginLogsById(int id) throws ResourceNotFoundException {

        LoginLog log = loginLogRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Login Log not found!"));
        loginLogRepository.delete(log);

    }

    //-------------------------Model Mapper------------------------

        //----------------User----------------------


    private List<UserDto> convertToUserDtoList(List<User> userList){
        return userList.stream().map(this::convertToDto).toList();
    }


    private UserDto convertToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }


        //---------------College----------------

    public List<CollegeDto> convertToCollegeDtoList(List<College> collegeList){
        return collegeList.stream().map(this::convertToDto).toList();
    }

    public CollegeDto convertToDto(College college){
        return modelMapper.map(college, CollegeDto.class);
    }
}
