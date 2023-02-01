package com.jesusmurguia.chat_app.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "message")
@ToString
public class Message {
    @Id
    @Column(name = "idmessage")
    private String id;

    @Size(max = 280)
    @NotBlank
    private String content;

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "iduser")
    private User sender;

    @OneToOne
    @JoinColumn(name = "receiver", referencedColumnName = "iduser")
    private User receiver;

    @NotBlank
    private String status;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime date;
}
