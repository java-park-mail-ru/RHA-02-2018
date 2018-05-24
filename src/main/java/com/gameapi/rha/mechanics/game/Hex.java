package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameapi.rha.mechanics.base.Coords;

import javax.validation.constraints.NotNull;

public class Hex extends GameObject {

    private Integer owner;

    @NotNull
    private Integer units;

    @NotNull
    private Coords coords;

    @NotNull
    private Integer type;

    //Different types:
    //0:water(no can simply walk into water(Vice city version))
    //1:grass field(grain or other crops, the simplest terrain)
    //2:sand desert(almost no reinforcements here, defending is harder)
    //3:forest (easy to defend, less reinforcements)
    //4:grass hills (easier to defend, normal reinforcements as well)
    //5:foresty hills ( wery easy to defend, as much reinforcements as in forest)
    //6:sand hills (a bit better then send)
    //7:mountains(the best thing to defend, but no reinforcements)
    //8:city(good thing to defend, and lots of reinforcements too)

    @JsonCreator
    public Hex(@JsonProperty("owner") @NotNull Integer owner, @JsonProperty("units") @NotNull Integer units,
                @JsonProperty("type") @NotNull Integer type, @JsonProperty("coords") @NotNull Coords coords) {
        this.owner = owner;
        this.units = units;
        this.coords = coords;
        this.type = type;


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



    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }
}
