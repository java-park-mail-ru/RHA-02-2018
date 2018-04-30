package com.gameapi.rha.websocket;

import javax.validation.constraints.NotNull;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull String forUser) throws HandleException;

    <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}