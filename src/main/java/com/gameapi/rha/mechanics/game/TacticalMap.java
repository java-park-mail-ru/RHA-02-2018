package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gameapi.rha.mechanics.services.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
