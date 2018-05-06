package com.gameapi.rha.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RemotePointService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerUser(@NotNull String user, @NotNull WebSocketSession webSocketSession) {
        sessions.put(user, webSocketSession);
    }

    public boolean isConnected(@NotNull String user) {
        return sessions.containsKey(user) && sessions.get(user).isOpen();
    }

    public void removeUser(@NotNull String user) {
        sessions.remove(user);
    }

    public void cutDownConnection(@NotNull String user, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(user);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(@NotNull String user, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(user);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + user);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        //noinspection OverlyBroadCatchBlock
        try {
            //noinspection ConstantConditions
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            throw new IOException("Unnable to send message", e);
        }
    }
}