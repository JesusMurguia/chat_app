package com.jesusmurguia.chat_app.business;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "message")
@Builder
public class Message {
    @Id
    @Column(name = "idmessage")
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;

    private String sender;

    private String receiver;

    private String content;

    private String status;

    private String room;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
