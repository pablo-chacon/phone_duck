package com.example.phone_duck.config;

import com.example.phone_duck.ws.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {
    @Autowired
    private SocketHandler socketHandler;

    private final static String SOCKET_PREFIX = "/sub";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, SOCKET_PREFIX + "/channels");
        registry.addHandler(socketHandler, SOCKET_PREFIX + "/chat/");
    }
}