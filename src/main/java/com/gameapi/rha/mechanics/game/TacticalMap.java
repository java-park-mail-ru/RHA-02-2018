package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public class TacticalMap extends GameObject {

    private List<Hex> map = new ArrayList<>();

    @JsonCreator
    public TacticalMap(List<Hex> map) {
        this.map = map;

    }

    public List<Hex> getMap() {
        return map;
    }

    public void setMap(List<Hex> map) {
        this.map = map;
    }
}
