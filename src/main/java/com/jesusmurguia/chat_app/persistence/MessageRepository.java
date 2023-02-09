package com.jesusmurguia.chat_app.persistence;

import com.jesusmurguia.chat_app.business.Conversation;
import com.jesusmurguia.chat_app.business.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    Set<Message> findAllBySenderInAndReceiverInAndRoomOrderByDateAsc(String[] sender, String[] receiver, String room);
}