package com.jesusmurguia.chat_app.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "conversation")
@Builder
public class Conversation {
    @Id
    @Column(name = "idconversation")
    private String id;

    private String sender;

    private String receiver;

    private String room;

    private int unreadMessages = 0;

    public void incrementUnreadMessages(){
        unreadMessages++;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "conversation_has_message",
            joinColumns = @JoinColumn(name = "idconversation"),
            inverseJoinColumns = @JoinColumn(name = "idmessage"))
    Set<Message> messages;

}
