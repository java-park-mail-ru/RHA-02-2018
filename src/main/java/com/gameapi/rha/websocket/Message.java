package com.gameapi.rha.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.mechanics.messages.input.JoinGame;
import com.gameapi.rha.mechanics.messages.output.FinishGame;
import com.gameapi.rha.mechanics.messages.output.InitGame;
import com.gameapi.rha.mechanics.messages.output.ServerStep;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(JoinGame.class),
        @JsonSubTypes.Type(InitGame.Request.class),
        @JsonSubTypes.Type(FinishGame.class),
        @JsonSubTypes.Type(ClientStep.class),
        @JsonSubTypes.Type(ServerStep.class),
        @JsonSubTypes.Type(ClientTurn.class),
})
public abstract class Message {
}

