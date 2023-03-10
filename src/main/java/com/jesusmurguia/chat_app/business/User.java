package com.jesusmurguia.chat_app.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {
    @Id
    @Column(name = "iduser")
    private String id;

    @Size(min = 4, max = 45)
    @NotBlank
    private String username;

    @JsonIgnore
    private String role;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    private String status;

    @JsonIgnore
    private String sessionId;
    @JsonIgnore
    private String room;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Room> rooms;

    public boolean equals(Object user){
        if(user instanceof User){
            User toCompare = (User) user;
            return this.id.equals(toCompare.id);
        }
        return false;
    }
}
