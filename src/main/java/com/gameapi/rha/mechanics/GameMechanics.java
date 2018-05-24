package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.input.ClientTurn;


import javax.validation.constraints.NotNull;

public interface GameMechanics {

    void step(@NotNull  String user, @NotNull ClientStep clientStep);

    void addUser(@NotNull String user, @NotNull Integer players);

    void reset();

    void turn(@NotNull  String user, @NotNull ClientTurn clientTurn);
}
