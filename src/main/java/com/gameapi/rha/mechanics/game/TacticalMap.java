package com.gameapi.rha.mechanics.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TacticalMap extends GameObject {

    private List<Hex> map = new ArrayList<>();

    public TacticalMap(){
        map.add(new Hex(1,1,2000, Arrays.asList(2,3),1));
        map.add(new Hex(2,0,1000, Arrays.asList(1,3,4,5),1));
        map.add(new Hex(3,0,1000, Arrays.asList(2,1,5),1));
        map.add(new Hex(4,0,1000, Arrays.asList(2,5),1));
        map.add(new Hex(5,2,2000, Arrays.asList(2,3,4),1));

    }

    public List<Hex> getMap() {
        return map;
    }

    public void setMap(List<Hex> map) {
        this.map = map;
    }
}
