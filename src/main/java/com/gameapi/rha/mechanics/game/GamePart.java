package com.gameapi.rha.mechanics.game;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

}
