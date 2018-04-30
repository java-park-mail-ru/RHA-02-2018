package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;

public interface GameMechanics {

    void Turn(@NotNull String userId, @NotNull ClientTurn clientSnap);

    void addUser(@NotNull String user);

    void gmStep(long frameTime);

    void reset();
}
