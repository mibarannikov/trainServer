package com.tasksbb.train.service;

import com.tasksbb.train.entity.User;
import com.tasksbb.train.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() ->new UsernameNotFoundException("Username not found with username: "+username));

        return  build(user);
    }

    public  User loadUserById(Long id){
        User user = userRepository.findUserById(id)
                .orElseThrow(() ->new UsernameNotFoundException("Username not found with id: "+id));

        return  build(user);
    }

    public  User loadById(Long id){
        return userRepository.findUserById(id).orElse(null);
    }

    public static User build(User user){
        List<GrantedAuthority> authorities = user
                .getRole()
                .stream()
                .map(role-> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        return new User(user.getId(),user.getUsername(), user.getEmail(), user.getPassword(), authorities);


    }
}
