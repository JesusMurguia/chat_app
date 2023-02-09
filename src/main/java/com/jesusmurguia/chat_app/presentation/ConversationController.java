package com.jesusmurguia.chat_app.presentation;

import com.jesusmurguia.chat_app.business.Conversation;
import com.jesusmurguia.chat_app.business.Room;
import com.jesusmurguia.chat_app.business.User;
import com.jesusmurguia.chat_app.persistence.ConvoRepository;
import com.jesusmurguia.chat_app.persistence.RoomRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class ConversationController {
    private static final Logger LOG = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    ConvoRepository convoRepo;

    @GetMapping("/api/chat/user/conversations/{room}")
    public ResponseEntity<?> getConversations(Authentication authentication, @PathVariable(required = true) String room ) {
        List<Conversation> conversations = convoRepo.findByReceiverAndRoom(authentication.getName(),room);
        return new ResponseEntity<>(conversations,HttpStatus.OK);
    }

    @PatchMapping("/api/chat/user/conversations/{room}")
    public ResponseEntity<?> setMessagesRead(Authentication authentication, @PathVariable(required = true) String room, @RequestParam String receiver ) {
        Conversation convo = convoRepo.findById(room+"_"+authentication.getName()+"_"+receiver);
        convo.setUnreadMessages(0);
        convoRepo.save(convo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}