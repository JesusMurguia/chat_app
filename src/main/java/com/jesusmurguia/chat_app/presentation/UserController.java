package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.LoginForm;
import com.jesusmurguia.chat_app.business.Room;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.persistence.RoomRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import com.jesusmurguia.chat_app.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/api/chat/room/users/{room}")
    public ResponseEntity<?> getToken(Authentication authentication, @PathVariable(required = true) String room, HttpServletResponse response) {
        Room selectedRoom = roomRepository.findById(room);
        if(selectedRoom.getUsers().isEmpty()){
            return new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK);
        }else{
            List<User> users = selectedRoom.getUsers();
            return new ResponseEntity<>(users,HttpStatus.OK);
        }
    }
}