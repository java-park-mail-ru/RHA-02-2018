package com.gameapi.rha.mechanics.messages.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameapi.rha.websocket.Message;

public class FinishGame extends Message {
    @JsonProperty
    private Integer player;

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }
}
