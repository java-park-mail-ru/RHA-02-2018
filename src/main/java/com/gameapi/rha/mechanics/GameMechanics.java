package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;

public interface GameMechanics {

    void Step(@NotNull  String user, @NotNull ClientStep clientStep);

    void addUser(@NotNull String user);

    void gmStep(long frameTime);

    void reset();
}
