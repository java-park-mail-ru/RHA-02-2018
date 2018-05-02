package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.mechanics.game.Hex;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ServerStep {

    @NotNull
    private List<Hex> map;

    @NotNull
    public List<Hex> getMap() {
        return map;
    }

}
