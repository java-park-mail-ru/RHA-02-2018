package com.gameapi.rha.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameapi.rha.mechanics.services.GameSessionService;
import com.gameapi.rha.services.UserService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.constraints.NotNull;
import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;



@Component
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    @Autowired
    private GameSessionService game;

    private final  @NotNull UserService userService;

    private final @NotNull MessageHandlerContainer messageHandlerContainer;

    private final @NotNull RemotePointService remotePointService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull UserService userService, @NotNull RemotePointService remotePointService,
                             ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.userService = userService;
        this.remotePointService = remotePointService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final String user = (String) webSocketSession.getAttributes().get("user");
        if ((user == null) || (userService.userInfo(user) == null)) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Openning websocket session is denied.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        remotePointService.registerUser(user, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final String user = (String) webSocketSession.getAttributes().get("user");
        if (user == null || userService.userInfo(user) == null) {
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(String user, TextMessage text) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at game response", ex);
            return;
        }
        try {
            //noinspection ConstantConditions
            messageHandlerContainer.handle(message, user);
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getClass().getName() + " with content: " + text, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final String user = (String) webSocketSession.getAttributes().get("user");
        //TODO:  Переподключение
        game.finishGame( game.getSessionForUser(user) );
        if (user == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        remotePointService.removeUser(user);
    }

    @SuppressWarnings("SameParameterValue")
    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status = closeStatus == null ? SERVER_ERROR : closeStatus;
        //noinspection OverlyBroadCatchBlock
        try {
            session.close(status);
        } catch (Exception ignore) {
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
