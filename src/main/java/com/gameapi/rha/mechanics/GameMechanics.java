package com.gameapi.rha.mechanics;

import javax.validation.constraints.NotNull;

public class GameMechanics {

//    void addClientSnapshot(@NotNull String userId, @NotNull ClientSnap clientSnap);

    public void addUser(@NotNull String user);

    void gmStep(long frameTime);

    void reset();

}
