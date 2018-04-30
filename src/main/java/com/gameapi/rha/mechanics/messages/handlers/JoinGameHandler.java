package com.gameapi.rha.mechanics.messages.handlers;

import com.gameapi.rha.mechanics.GameMechanics;
import com.gameapi.rha.mechanics.messages.inbox.JoinGame;
import com.gameapi.rha.websocket.MessageHandler;
import com.gameapi.rha.websocket.MessageHandlerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    @NotNull
    private final GameMechanics gameMechanics;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(@NotNull JoinGame.Request message, @NotNull String User) {
        gameMechanics.addUser(User);
    }
}