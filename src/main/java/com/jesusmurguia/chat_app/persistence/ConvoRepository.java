package com.jesusmurguia.chat_app.persistence;

import com.jesusmurguia.chat_app.business.Conversation;
import com.jesusmurguia.chat_app.business.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConvoRepository extends CrudRepository<Conversation, Long> {
    Conversation findById(String id);

    List<Conversation> findByReceiverAndRoom(String receiver, String room);
    Boolean existsById(String id);
}