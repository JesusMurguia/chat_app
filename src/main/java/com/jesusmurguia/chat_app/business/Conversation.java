package com.jesusmurguia.chat_app.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "conversation")
@ToString
public class Conversation {
    @Id
    @Column(name = "idconversation")
    private String id;

    @OneToMany
    @JoinColumn(name = "idconversation")
    private List<Message> messages;

    @ManyToMany(mappedBy = "conversations")
    List<User> users;
}
