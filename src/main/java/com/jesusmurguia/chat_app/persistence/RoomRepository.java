package com.jesusmurguia.chat_app.persistence;

import com.jesusmurguia.chat_app.business.Room;
import com.jesusmurguia.chat_app.business.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findById(String id);
    boolean existsById(String username);
}