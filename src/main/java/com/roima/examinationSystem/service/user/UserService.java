package com.roima.examinationSystem.service.user;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.UserRepository;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public void addUser(AddUserRequest request) throws InvalidENUMException,ResourceExistsException {

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
            throw new InvalidENUMException("Invalid user role!");
        }

    }

    @Override
    public void updateUser(UpdateUserRequest request, int userId) throws InvalidENUMException, ResourceNotFoundException {


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
            throw new InvalidENUMException("Invalid user role!");
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
    public List<User> getUsersByRole(String role) throws InvalidENUMException {
        try {
            Role erole = Role.valueOf(role);
            return userRepository.findByRole(erole);
        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid user role!");
        }
    }

}
