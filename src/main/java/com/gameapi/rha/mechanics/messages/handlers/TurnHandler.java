package com.gameapi.rha.mechanics.messages.handlers;

import com.gameapi.rha.mechanics.GameMechanics;
import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.websocket.MessageHandler;
import com.gameapi.rha.websocket.MessageHandlerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class TurnHandler extends MessageHandler<ClientTurn> {
    private final @NotNull GameMechanics gameMechanics;
    private final @NotNull MessageHandlerContainer messageHandlerContainer;

    public TurnHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ClientTurn.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ClientTurn.class, this);
    }

    @Override
    public void handle(@NotNull ClientTurn message, @NotNull String user) {
        gameMechanics.turn(user, message);
    }
}
