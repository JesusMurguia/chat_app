package com.jesusmurguia.chat_app.presentation;


import com.jesusmurguia.chat_app.business.Room;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.business.UserStatus;
import com.jesusmurguia.chat_app.business.UserUpdateForm;
import com.jesusmurguia.chat_app.persistence.RoomRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Objects;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserRepository userRepo;
    @MessageMapping("/user_update/{idroom}")
    public void processMessage(@DestinationVariable String idroom, @Payload UserUpdateForm data) {
        User user = userRepo.findByUsername(data.getUsername());
        user.setStatus(data.getStatus());
        userRepo.save(user);
        LOG.info(data.getUsername()+" joined the chatroom: "+idroom + "with status: "+user.getStatus());
        messagingTemplate.convertAndSend("/get_user_updates/"+idroom, data);
    }
    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = Objects.requireNonNull(headers.getNativeHeader("username")).get(0);
        String room = Objects.requireNonNull(headers.getNativeHeader("room")).get(0);
        User user = userRepo.findByUsername(username);
        user.setRoom(room);
        user.setSessionId(sessionId);
        userRepo.save(user);
        LOG.info(sessionId);
    }

    @EventListener
    private void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        if(userRepo.existsBySessionId(sessionId)){
            User user = userRepo.findBySessionId(sessionId);
            String room = user.getRoom();
            user.setSessionId(null);
            user.setRoom(null);
            user.setStatus(String.valueOf(UserStatus.OFFLINE));
            userRepo.save(user);
            messagingTemplate.convertAndSend("/get_user_updates/"+room, new UserUpdateForm());
        }
    }
}