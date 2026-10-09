package com.example.taskmanager.service.implementation;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.RegLoginRequest;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.exceptions.BadRequestException;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ApiResponse<?> register(RegLoginRequest regRequest) {
        if (userRepository.findByEmail(regRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setEmail(regRequest.getEmail());
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));

        if (regRequest.getRole() == null) {
            user.setRole(Role.USER);
        } else if (regRequest.getRole().equals(Role.ADMIN)) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setEmail(savedUser.getEmail());
        savedUserDTO.setRole(savedUser.getRole());

        return new ApiResponse<>(201, "User Saved Successfully", savedUserDTO);
    }

    @Override
    public ApiResponse<?> login(RegLoginRequest loginRequest, HttpServletRequest request) {

        // it calls the customeruserdetailsservice and customeruserdetails class created to validate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // save the info to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // creates a cookie session that is the JESSIONID for that user
        // it is going to automatically pass the session down when you are accessing the endpoint
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // returns the response to the controller
        return new ApiResponse<>(200, "Logged in Successfully", null);
    }
}
