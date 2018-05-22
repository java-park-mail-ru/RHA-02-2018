package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ServerStep extends Message {

    @NotNull
    private String type;

    @NotNull
    private List<Hex> map;

    @NotNull
    public List<Hex> getMap() {
        return map;
    }

    public void setMap(List<Hex> map) {
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
