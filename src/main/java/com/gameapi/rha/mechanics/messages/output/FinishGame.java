package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.websocket.Message;

public class FinishGame extends Message {
    private Integer player;

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }
}
