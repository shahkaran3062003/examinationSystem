package com.roima.examinationSystem.auth;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.LoginLog;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import com.roima.examinationSystem.repository.LoginLogRepository;
import com.roima.examinationSystem.repository.UserRepository;
import com.roima.examinationSystem.request.LoginRequest;
import com.roima.examinationSystem.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginLogRepository loginLogRepository;
    private final HttpServletRequest request;


    public String login(LoginRequest loginRequest) throws ResourceNotFoundException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            if(user.getRole()==Role.STUDENT) {
                log(user.getId(), loginRequest.getEmail(), request.getRemoteAddr(), true);
            }
            return jwtService.generateToken(user);
        }catch (Exception e) {
            log(-1,loginRequest.getEmail(),request.getRemoteAddr(),false);
            throw e;
        }
    }


    //TODO: remove register endpoint if not needed
    public String register(RegisterRequest registerRequest) throws InvalidValueException, ResourceExistsException {

        try {
            Role role = Role.valueOf(registerRequest.getRole());

            if(userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new ResourceExistsException("User already exists!");
            }

            User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), role);
            userRepository.save(user);
            return jwtService.generateToken(user);

        }catch(IllegalArgumentException e) {
            throw new InvalidValueException("Invalid role");
        }
    }

    public void log(int userId,String userEmail,String ip, boolean success) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUserEmail(userEmail);
        loginLog.setIp(ip);
        loginLog.setSuccess(success);
        loginLog.setTimestamp(LocalDateTime.now());
        loginLogRepository.save(loginLog);
    }
}
