package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameapi.rha.mechanics.base.Coords;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hex extends GameObject {

    private Integer owner;

    @NotNull
    private Integer units;

    @NotNull
    private Coords coords;

    @NotNull
    private Integer type;

    @NotNull
    private List<List<Integer>> neibours = new ArrayList<>();

    @JsonCreator
    public Hex(@JsonProperty("owner") @NotNull Integer owner, @JsonProperty("units") @NotNull Integer units,
                @JsonProperty("type") @NotNull Integer type, @JsonProperty("coords") @NotNull Coords coords) {
        this.owner = owner;
        this.units = units;
        this.coords=coords;
        this.type = type;

        if(coords.getY() - 1>=0) {
            neibours.add(Arrays.asList(coords.getY() - 1, coords.getX()));
        }
        if(coords.getX() - 1>=0) {
            neibours.add(Arrays.asList(coords.getY(), coords.getX() - 1));
        }
        neibours.add(Arrays.asList(coords.getY(), coords.getX() + 1));
        neibours.add(Arrays.asList(coords.getY() + 1, coords.getX()));
        if(coords.getX() % 2 == 1 && coords.getY() - 1>=0){
            if(coords.getX() - 1>=0) {
                neibours.add(Arrays.asList(coords.getY() - 1, coords.getX() - 1));
            }
            neibours.add(Arrays.asList(coords.getY() - 1, coords.getX() + 1));

        } else{
            if(coords.getX() - 1>=0) {
                neibours.add(Arrays.asList(coords.getY() + 1, coords.getX() - 1));
            }
            neibours.add(Arrays.asList(coords.getY() + 1, coords.getX() + 1));
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public List<List<Integer>> getNeibours() {
        return neibours;

    }
}
