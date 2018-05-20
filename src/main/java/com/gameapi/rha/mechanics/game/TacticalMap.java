package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class TacticalMap extends GameObject {

    private List<List<Hex>> map;

    @JsonCreator
    public TacticalMap(List<List<Hex>> map) {
        this.map = map;

    }

    public List<List<Hex>> getMap() {
        return map;
    }

    public void setMap(List<List<Hex>> map) {
        this.map = map;
    }
}
