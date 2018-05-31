package com.gameapi.rha.mechanics.messages.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameapi.rha.websocket.Message;

public class JoinGame extends Message {
    private Integer players;

    @JsonCreator
    public JoinGame(@JsonProperty("players") Integer players) {
        this.players = players;
        if (this.players == null) {
            this.players = 2;
        }
    }

    public Integer getPlayers() {
        return players;
    }
}
