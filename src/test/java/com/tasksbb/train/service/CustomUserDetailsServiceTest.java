package com.tasksbb.train.service;

import com.tasksbb.train.entity.User;
import com.tasksbb.train.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;
    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);

    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        customUserDetailsService.loadUserByUsername("test");
        verify(userRepository,times(1)).findUserByEmail(anyString());
    }

    @Test
    void loadUserById() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(new User()));
        customUserDetailsService.loadUserById(1L);
        verify(userRepository,times(1)).findUserById(anyLong());
    }

}
