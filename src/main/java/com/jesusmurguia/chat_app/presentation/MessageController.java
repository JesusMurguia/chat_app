package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.Conversation;
import com.jesusmurguia.chat_app.business.Message;
import com.jesusmurguia.chat_app.persistence.ConvoRepository;
import com.jesusmurguia.chat_app.persistence.MessageRepository;
import com.jesusmurguia.chat_app.persistence.RoomRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class MessageController {
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    MessageRepository messageRepository;
    @GetMapping("/api/chat/messages/{room}")
    public ResponseEntity<?> getConversations(Authentication authentication, @PathVariable(required = true) String room, @RequestParam String receiver) {
        LOG.error(room);
        LOG.error(receiver);
        String[] users = {authentication.getName(),receiver};
        Set<Message> messages = messageRepository.findAllBySenderInAndReceiverInAndRoomOrderByDateAsc(users, users, room);
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }
}