package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.messages.input.ClientStep;


import javax.validation.constraints.NotNull;

public interface GameMechanics {

    void step(@NotNull  String user, @NotNull ClientStep clientStep);

    void addUser(@NotNull String user);

    void gmStep(long frameTime);

    void reset();
}
