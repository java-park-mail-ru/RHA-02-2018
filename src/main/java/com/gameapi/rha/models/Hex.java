package com.gameapi.rha.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hex {
    private Integer id;
    private Integer type;
    private Integer[] neibours;
    private String owner;
    private Integer unit;

    @JsonCreator
    Hex(@JsonProperty("id") Integer id,
        @JsonProperty("type") Integer type,
        @JsonProperty("neibours") Integer[] neibours,
        @JsonProperty("owner") String owner,
        @JsonProperty("unit") Integer unit){
        this.id=id;
        this.type=type;
        this.neibours=neibours;
        this.owner=owner;
        this.unit=unit;
    }


    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer[] getNeibours() {
        return neibours;
    }

    public void setNeibours(Integer[] neibours) {
        this.neibours = neibours;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
