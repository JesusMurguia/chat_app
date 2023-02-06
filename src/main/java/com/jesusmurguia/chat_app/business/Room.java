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
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "idroom")
    private String id;

    @OneToMany
    @JoinColumn(name = "idroom")
    private List<Conversation> conversations;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "room_has_user",
            joinColumns = @JoinColumn(name = "iduser"),
            inverseJoinColumns = @JoinColumn(name = "idroom"))
    List<User> users;
}
