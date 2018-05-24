package com.gameapi.rha.mechanics.messages.input;

import com.gameapi.rha.websocket.Message;

public class JoinGame extends Message {
    private Integer players;

    public JoinGame(Integer players) {
        this.players = players;
        if (this.players == null) {
            this.players = 2;
        }
    }

    public Integer getPlayers() {
        return players;
    }
}
