package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.LoginForm;
import com.jesusmurguia.chat_app.business.Room;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.persistence.RoomRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import com.jesusmurguia.chat_app.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jdk.swing.interop.SwingInterOpUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    RoomRepository roomRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<?> getToken(Authentication authentication,@RequestBody(required = true) String room, HttpServletResponse response) {
        LOG.debug("Token requested for user: '{}'", authentication.getName());
        room = handleRoom(room, userRepo.findByUsername(authentication.getName()));
        String token  = tokenService.generateToken(authentication, room);
        LOG.debug("Token granted {}", token);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(Map.of("accessToken", token, "room", room),HttpStatus.OK);
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

    private String handleRoom(String idroom, User user){
        if(roomRepository.existsById(idroom)){
            Room room = roomRepository.findById(idroom);
            if(!room.getUsers().contains(user)){
                room.getUsers().add(user);
                roomRepository.save(room);
            }
        }else{
            Room newRoom = new Room();
            idroom = UUID.randomUUID().toString();
            newRoom.setId(idroom);
            newRoom.setUsers(new ArrayList<>());
            newRoom.getUsers().add(user);
            roomRepository.save(newRoom);
        }
        return idroom;
    }
}