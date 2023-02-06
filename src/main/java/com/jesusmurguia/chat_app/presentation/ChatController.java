package com.jesusmurguia.chat_app.presentation;


import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.business.UserUpdateForm;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserRepository userRepo;
    @MessageMapping("/user_update/{idroom}")
    public void processMessage(@DestinationVariable String idroom, @Payload UserUpdateForm data) {
        LOG.info(data.getUsername()+" joined the chatroom: "+idroom);
        messagingTemplate.convertAndSend("/get_user_updates/"+idroom, data);
    }
}