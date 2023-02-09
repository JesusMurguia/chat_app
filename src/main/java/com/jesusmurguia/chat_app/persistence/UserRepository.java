package com.jesusmurguia.chat_app.persistence;

import com.jesusmurguia.chat_app.business.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findById(String id);
    boolean existsByUsername(String username);

    User findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);
}