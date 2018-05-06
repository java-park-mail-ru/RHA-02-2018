package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Hex extends GameObject {

    private Integer owner;

    @NotNull
    private Integer units;

    @NotNull
    private List<Integer> neibours;

    @NotNull
    private Integer type;

    @JsonCreator
    public Hex(@NotNull Integer owner, @NotNull Integer units, @NotNull List<Integer> neibours, @NotNull Integer type) {
        this.owner = owner;
        this.units = units;
        this.neibours = neibours;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getNeibours() {
        return neibours;
    }

    public void setNeibours(List<Integer> neibours) {
        this.neibours = neibours;
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

}
