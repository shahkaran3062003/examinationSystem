package com.roima.examinationSystem.service.user;

import com.roima.examinationSystem.exception.InvalidRoleException;
import com.roima.examinationSystem.exception.UserExistsException;
import com.roima.examinationSystem.exception.UserNotFoundException;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.UserRepository;
import com.roima.examinationSystem.request.AddUserRequest;
import com.roima.examinationSystem.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public void addUser(AddUserRequest request) throws InvalidRoleException, UserExistsException {

        try {

            User user = getUserByEmail(request.getEmail());
            if (user != null) {
                throw new UserExistsException("User already exists!");
            }
            User newUser = new User(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    Role.valueOf(request.getRole()));
            userRepository.save(newUser);
        }catch (IllegalArgumentException e){
            throw new InvalidRoleException("Invalid user role!");
        } catch (UserExistsException e) {
            throw new UserExistsException("User already exists!");
        }

    }

    @Override
    public void updateUser(UpdateUserRequest request, int userId) throws InvalidRoleException {


        try {
            User user = getUserById(userId);

            Role role = Role.valueOf(request.getRole());

            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setRole(role);
            userRepository.save(user);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException("User not found!");
        }catch (IllegalArgumentException e){
            throw new InvalidRoleException("Invalid user role!");
        }



//        try {
//            Optional.ofNullable(getUserById(userId)).map(oldUser -> {
//
//                oldUser.setUsername(request.getUsername());
//                oldUser.setEmail(request.getEmail());
//                oldUser.setPassword(request.getPassword());
//                oldUser.setRole(Role.valueOf(request.getRole()));
//                return oldUser;
//            }).map(userRepository::save).orElseThrow(() -> new RuntimeException("Internal server error!"));
//
//        }catch (InvalidRoleException e){
//            throw new InvalidRoleException("Invalid user role!");
//        }
    }

    @Override
    public void deleteUserById(int id) {

        userRepository.findById(id).ifPresentOrElse(userRepository::delete,()-> {
            throw new UserNotFoundException("User not found!");});
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found!"));
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found!");
        }else {
            return userRepository.findByEmail(email);
        }

    }

    @Override
    public List<User> getUsersByRole(String role) throws InvalidRoleException {
        try {
            Role erole = Role.valueOf(role);
            return userRepository.findByRole(erole);
        }catch (Exception e){
            throw new InvalidRoleException("Invalid user role!");
        }
    }

}
