package org.example.messageme.controller;


import lombok.RequiredArgsConstructor;
import org.example.messageme.dto.JwtRequest;
import org.example.messageme.dto.JwtResponse;
import org.example.messageme.dto.RegisterRequest;
import org.example.messageme.dto.UserDTO;
import org.example.messageme.dto.validation.OnCreate;
import org.example.messageme.entity.User;
import org.example.messageme.mapper.UserMapper;
import org.example.messageme.repository.UserRepository;
import org.example.messageme.service.AuthService;
import org.example.messageme.service.EmailVerificationService;
import org.example.messageme.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
//    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;


    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest request) {
        return authService.login(request);
    }

//    @PostMapping("/register")
//    public UserDTO register(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
//        userDTO.setId(null);
//        User user = userMapper.toEntity(userDTO);
//        user = userService.create(user);
//        return userMapper.toDto(user);
//    }

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Email already registered");
//        }
//        user.setId(null);
//
//        user.setEmailVerified(false);
//        user = userService.create(user);
//        emailVerificationService.sendVerificationEmail(user);
//        return ResponseEntity.ok("Verification link sent to " + user.getEmail());
//    }

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Email already registered");
//        }
//
//        user.setId(null);
//        user.setEmailVerified(false);
//        user = userService.create(user);  // ← Внутри уже отправляется email
//
//        // УДАЛИТЕ ЭТУ СТРОКУ:
//        // emailVerificationService.sendVerificationEmail(user);
//
//        return ResponseEntity.ok("Verification link sent to " + user.getEmail());
//    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Validated @RequestBody RegisterRequest request) {
        try {
            // Создаем User из DTO
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPhone(request.getPhone());
            user.setPassword(request.getPassword());

            user = userService.create(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Registration successful",
                    "email", user.getEmail(),
                    "info", "Verification link sent to your email"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String result = emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@Validated String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}

//package org.example.messageme.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.messageme.dto.JwtRequest;
//import org.example.messageme.dto.JwtResponse;
//import org.example.messageme.dto.RegisterRequest;
//import org.example.messageme.entity.User;
//import org.example.messageme.service.EmailVerificationService;
//import org.example.messageme.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//@Validated
//public class AuthController {
//
//    private final UserService userService;
//    private final EmailVerificationService emailVerificationService;
//
//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setUsername(request.getUsername());
//        user.setPhone(request.getPhone());
//        user.setPassword(request.getPassword());
//
//        user = userService.create(user);
//        return ResponseEntity.ok(Map.of(
//                "message", "Registration successful",
//                "email", user.getEmail(),
//                "verified", "false (check your inbox)"
//        ));
//    }
//
//    @GetMapping("/verify-email")
//    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
//        String result = emailVerificationService.verifyEmail(token);
//        return ResponseEntity.ok(result);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
//        try {
//            return ResponseEntity.ok(userService.getByEmail(request.getEmail()));
//        } catch (Exception e) {
//            return ResponseEntity.status(403).body(Map.of("error", "Login not allowed until verified."));
//        }
//    }
//        @PostMapping("/refresh")
//    public JwtResponse refresh(@Validated String refreshToken) {
//        return authService.refreshToken(refreshToken);
//    }
//}


