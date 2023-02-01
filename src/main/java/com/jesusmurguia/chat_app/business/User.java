package com.jesusmurguia.chat_app.business;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Size(min = 4, max = 45)
    private String password;

    @NotBlank
    private String status;
}
