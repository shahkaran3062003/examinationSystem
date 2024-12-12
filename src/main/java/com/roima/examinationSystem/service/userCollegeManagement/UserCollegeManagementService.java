package com.roima.examinationSystem.service.userCollegeManagement;


import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.CollegeRepository;
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

    //-----------------------------------User----------------------------------------------
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public void addUser(AddUserRequest request) throws InvalidValueException, ResourceExistsException {

        try {
            boolean isUserPresent = userRepository.existsByEmail(request.getEmail());
            if (isUserPresent) {
                throw new ResourceExistsException("User already exists!");
            }
            User newUser = new User(
                    request.getUsername(),
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
            User user = getUserById(userId);

            Role role = Role.valueOf(request.getRole());

            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setRole(role);
            userRepository.save(user);
        } catch (ResourceNotFoundException  e) {
            throw e;
        } catch(IllegalArgumentException e){
            throw new InvalidValueException("Invalid user role!");
        }

    }

    @Override
    public void deleteUserById(int id) throws ResourceNotFoundException {

        try
        {
            User user = getUserById(id);
            userRepository.delete(user);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Override
    public User getUserById(int id) throws ResourceNotFoundException {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User getUserByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new ResourceNotFoundException("User not found!");
        }else {
            return userRepository.findByEmail(email);
        }

    }

    @Override
    public List<User> getUsersByRole(String role) throws InvalidValueException {
        try {
            Role erole = Role.valueOf(role);
            return userRepository.findByRole(erole);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid user role!");
        }
    }


    //----------------------------------------College------------------------------

    @Override
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    @Override
    public College getCollegeById(int id) throws ResourceNotFoundException {
        return collegeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("College not found!"));
    }

    @Override
    public College getCollegeByName(String name) throws ResourceNotFoundException {

        College college = collegeRepository.findByName(name);

        if(college== null){
            throw new ResourceNotFoundException("College not found!");
        }else{
            return college;
        }
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
        College oldCollege = getCollegeById(id);

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
        College college = getCollegeById(id);
        collegeRepository.delete(college);
    }

    public List<CollegeDto> getConvertedDtoList(List<College> collegeList){
        return collegeList.stream().map(this::convertToDto).toList();
    }

    public CollegeDto convertToDto(College college){
        return modelMapper.map(college, CollegeDto.class);
    }
}
