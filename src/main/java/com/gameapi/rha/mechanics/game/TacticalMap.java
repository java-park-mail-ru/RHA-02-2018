package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gameapi.rha.mechanics.base.Coords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TacticalMap extends GameObject {

    private List<List<Hex>> map;

    @JsonCreator
    public TacticalMap(List<List<Hex>> map) {
        this.map = map;

    }

    public List<List<Integer>> getNeighbours(Hex hex) {
        List<List<Integer>> neighbours = new ArrayList<>();
        Coords coords = hex.getCoords();
        if (coords.getY() - 1 >= 0) {
            neighbours.add(Arrays.asList(coords.getY() - 1, coords.getX()));
        }
        if (coords.getX() - 1 >= 0) {
            neighbours.add(Arrays.asList(coords.getY(), coords.getX() - 1));
        }
        if (coords.getX() + 1 < map.get(0).size()) {
            neighbours.add(Arrays.asList(coords.getY(), coords.getX() + 1));
        }
        if (coords.getY() + 1 < map.size()) {
            neighbours.add(Arrays.asList(coords.getY() + 1, coords.getX()));
        }
        if (coords.getX() % 2 == 1 && coords.getY() - 1 >= 0) {
            if (coords.getX() - 1 >= 0) {
                neighbours.add(Arrays.asList(coords.getY() - 1, coords.getX() - 1));
            }
            neighbours.add(Arrays.asList(coords.getY() - 1, coords.getX() + 1));

        } else {
            if (coords.getX() - 1 >= 0) {
                if (coords.getY() + 1 < map.size()) {
                    neighbours.add(Arrays.asList(coords.getY() + 1, coords.getX() - 1));
                }
            }
            if (coords.getX() + 1 < map.get(0).size() && coords.getY() + 1 < map.size()) {
                neighbours.add(Arrays.asList(coords.getY() + 1, coords.getX() + 1));
            }
        }
        return neighbours;

    }

    public Hex get(Integer x, Integer y) {
        return map.get(y).get(x);
    }

    public List<List<Hex>> getMap() {
        return map;
    }

    public void setMap(List<List<Hex>> map) {
        this.map = map;
    }
}
