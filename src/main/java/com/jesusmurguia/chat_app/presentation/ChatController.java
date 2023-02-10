package com.jesusmurguia.chat_app.presentation;


import com.jesusmurguia.chat_app.business.*;
import com.jesusmurguia.chat_app.persistence.ConvoRepository;
import com.jesusmurguia.chat_app.persistence.MessageRepository;
import com.jesusmurguia.chat_app.persistence.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserRepository userRepo;

    @Autowired
    ConvoRepository convoRepo;

    @Autowired
    MessageRepository messageRepo;

    @MessageMapping("/message")
    public void processMessage(@Payload Message chatMessage) {
        Message savedMessage = messageRepo.save(chatMessage);
        handleConvos(savedMessage, chatMessage.getSender(), chatMessage.getReceiver());
        handleConvos(savedMessage, chatMessage.getReceiver(), chatMessage.getSender());
    }

    @MessageMapping("/read_messages/{username}/{room}")
    public void processMessage(@DestinationVariable String username,@DestinationVariable String room,@Payload String receiver) {
        System.out.println(room+"_"+username+"_"+receiver);
        Conversation convo = convoRepo.findById(room+"_"+receiver+"_"+username);
        convo.setUnreadMessages(0);
        convoRepo.save(convo);
        messagingTemplate.convertAndSendToUser(username,"/read_messages/"+room,true);
    }

    @Transactional
    private void handleConvos(Message chatMessage, String sender, String receiver){
        String senderConvoId = chatMessage.getRoom()+"_"+sender+"_"+receiver;
        Conversation senderConv;
        if(convoRepo.existsById(senderConvoId)){
            senderConv = convoRepo.findById(senderConvoId);
            senderConv.getMessages().add(chatMessage);
        }else{
            senderConv = Conversation.builder()
                    .id(senderConvoId)
                    .sender(sender)
                    .receiver(receiver)
                    .room(chatMessage.getRoom())
                    .messages(Set.of(chatMessage))
                    .build();
        }
        if(receiver.equals(chatMessage.getReceiver())){
            senderConv.incrementUnreadMessages();
        }
        senderConv.setLastMessage(chatMessage.getContent());
        convoRepo.save(senderConv);
        messagingTemplate.convertAndSendToUser(receiver,"/messages/"+chatMessage.getRoom(),chatMessage);
    }

    @MessageMapping("/user_update/{idroom}")
    public void processUserUpdate(@DestinationVariable String idroom, @Payload UserUpdateForm data) {
        User user = userRepo.findByUsername(data.getUsername());
        user.setStatus(data.getStatus());
        userRepo.save(user);
        LOG.info(data.getUsername()+" joined the chatroom: "+idroom + "with status: "+user.getStatus());
        messagingTemplate.convertAndSend("/get_user_updates/"+idroom, data);
    }
    @EventListener
    protected void handleSessionConnected(SessionConnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        if(!userRepo.existsBySessionId(sessionId)) {
            SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
            String username = Objects.requireNonNull(headers.getNativeHeader("username")).get(0);
            String room = Objects.requireNonNull(headers.getNativeHeader("room")).get(0);
            User user = userRepo.findByUsername(username);
            user.setRoom(room);
            user.setSessionId(sessionId);
            userRepo.save(user);
            LOG.info(sessionId);
        }
    }

    @EventListener
    protected void handleSessionDisconnected(SessionDisconnectEvent event) {
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