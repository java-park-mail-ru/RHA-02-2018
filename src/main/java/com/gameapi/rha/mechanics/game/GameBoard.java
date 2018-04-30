package com.gameapi.rha.mechanics.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameBoard {
    private Integer id;
    private Hex[] gameF;


    @JsonCreator
    GameBoard(@JsonProperty("id") Integer id,
        @JsonProperty("gameF") Hex[] neibours){
        this.id=id;
        this.gameF=neibours;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hex[] getGameF() {
        return gameF;
    }

    public void setGameF(Hex[] gameF) {
        this.gameF = gameF;
    }
}
