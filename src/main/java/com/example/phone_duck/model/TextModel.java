package com.example.phone_duck.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TextModel {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.AUTO) // AUTO_INCREMENT
    private long id;
    private String name;
    private String channelIds;

    private WebSocketSession session;
}