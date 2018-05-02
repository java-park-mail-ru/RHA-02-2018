package com.gameapi.rha.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gameapi.rha.mechanics.messages.input.JoinGame;
import com.gameapi.rha.mechanics.messages.output.FinishGame;
import com.gameapi.rha.mechanics.messages.output.InitGame;
//import com.gameapi.rha.mechanics.messages.inbox.JoinGame;
//import com.gameapi.rha.mechanics.messages.outbox.FinishGame;
//import com.gameapi.rha.mechanics.messages.outbox.InitGame;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(JoinGame.Request.class),
        @JsonSubTypes.Type(InitGame.Request.class),
        @JsonSubTypes.Type(FinishGame.class),
})
public abstract class Message {
}

