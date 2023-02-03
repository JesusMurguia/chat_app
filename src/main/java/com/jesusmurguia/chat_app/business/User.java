package com.jesusmurguia.chat_app.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
@ToString
public class User {
    @Id
    @Column(name = "iduser")
    private String id;

    @Size(min = 4, max = 45)
    @NotBlank
    private String username;

    private String role;

    @NotBlank
    private String password;

    @NotBlank
    private String status;

    @ManyToMany(mappedBy = "users")
    List<Room> rooms;

    @ManyToMany
    @JoinTable(
            name = "user_has_conversation",
            joinColumns = @JoinColumn(name = "idconversation"),
            inverseJoinColumns = @JoinColumn(name = "iduser"))
    List<Conversation> conversations;
}
