package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.LoginForm;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RegistrationController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody LoginForm loginForm) {
        if(userRepo.existsByUsername(loginForm.getUsername())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(loginForm.getUsername());
        user.setPassword(passwordEncoder.encode(loginForm.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus("OFFLINE");
        userRepo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}