package com.jonathanlamela.springspaauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jonathanlamela.springspaauth.repository.UserRepository;
import com.jonathanlamela.springspaauth.entity.User;
import com.jonathanlamela.springspaauth.model.request.CreateUserRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public String getServiceVersion() {
        return "0.0.1";
    }

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean CreateUser(CreateUserRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            user = this.userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
