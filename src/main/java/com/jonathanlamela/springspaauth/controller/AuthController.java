package com.jonathanlamela.springspaauth.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathanlamela.springspaauth.entity.User;
import com.jonathanlamela.springspaauth.model.request.CreateUserRequest;
import com.jonathanlamela.springspaauth.model.request.LoginRequest;
import com.jonathanlamela.springspaauth.model.response.LoginResponse;
import com.jonathanlamela.springspaauth.repository.UserRepository;
import com.jonathanlamela.springspaauth.service.JwtService;
import com.jonathanlamela.springspaauth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/user/status")
    public ResponseEntity<User> getUserStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest requestModel) {

        Optional<User> user = userRepository.findByEmail(requestModel.getEmail());

        if (user.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        boolean result = this.userService.CreateUser(requestModel);

        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest requestModel) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestModel.getEmail(),
                            requestModel.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> user = userRepository.findByEmail(requestModel.getEmail());

        String jwtToken = jwtService.generateToken(user.get());

        LoginResponse response = new LoginResponse();

        if (requestModel.getLoginType().equals("cookie")) {
            response.setStatus("cookie generated");
            return ResponseEntity.ok().header("Set-Cookie", "jwt=" + jwtToken).body(response);
        } else if (requestModel.getLoginType().equals("jwt")) {
            response.setStatus("jwt generated");
            response.setJwtToken(jwtToken);
        }

        return ResponseEntity.ok(response);
    }

}
