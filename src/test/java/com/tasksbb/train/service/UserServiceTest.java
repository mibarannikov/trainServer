package com.tasksbb.train.service;

import com.tasksbb.train.entity.User;
import com.tasksbb.train.payload.request.SignupRequest;
import com.tasksbb.train.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userService = new UserService(userRepository,passwordEncoder);
    }

    @Test
    void createUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.test");
        signupRequest.setFirstname("Test");
        userService.createUser(signupRequest);
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void getCurrentUser() {
        Principal principal = () -> "test";
        User user = new User();
        user.setEmail("test@test.test");
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        userService.getCurrentUser(principal);
        verify(userRepository,times(1)).findUserByUsername(anyString());

    }
}
