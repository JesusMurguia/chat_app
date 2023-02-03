package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.LoginForm;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import com.jesusmurguia.chat_app.service.TokenService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.slf4j.Logger;

@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/api/token")
    public ResponseEntity<?> getToken(Authentication authentication,@RequestParam(required = true) String room) {
        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token  = tokenService.generateToken(authentication, room);
        LOG.debug("Token granted {}", token);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }

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