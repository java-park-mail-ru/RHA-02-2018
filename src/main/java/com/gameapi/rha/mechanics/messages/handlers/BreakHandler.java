package com.gameapi.rha.mechanics.messages.handlers;


import com.gameapi.rha.mechanics.messages.input.Break;
import com.gameapi.rha.websocket.MessageHandler;
import com.gameapi.rha.websocket.MessageHandlerContainer;
import com.gameapi.rha.websocket.RemotePointService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class BreakHandler extends MessageHandler<Break> {

    private final @NotNull RemotePointService remotePointService;
    private final @NotNull MessageHandlerContainer messageHandlerContainer;

    public BreakHandler(@NotNull RemotePointService gameSocketHandler, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(Break.class);
        this.remotePointService = gameSocketHandler;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Break.class, this);
    }

    @Override
    public void handle(@NotNull Break message, @NotNull String user) {
        remotePointService.cutDownConnection(user, new CloseStatus(400));
    }
}
