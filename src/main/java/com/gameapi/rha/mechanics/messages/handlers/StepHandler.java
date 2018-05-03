package com.gameapi.rha.mechanics.messages.handlers;

import com.gameapi.rha.mechanics.GameMechanics;
import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.input.JoinGame;
import com.gameapi.rha.websocket.MessageHandler;
import com.gameapi.rha.websocket.MessageHandlerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class StepHandler extends MessageHandler<ClientStep> {
    @NotNull
    private final GameMechanics gameMechanics;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;

    public StepHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ClientStep.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ClientStep.class, this);
    }

    @Override
    public void handle(@NotNull ClientStep message, @NotNull String user) {
        gameMechanics.Step(user,message);
    }
}

