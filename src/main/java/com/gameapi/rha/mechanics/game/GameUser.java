package com.gameapi.rha.mechanics.game;

import com.gameapi.rha.mechanics.services.MechanicsTimeService;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;

public class GameUser extends GameObject {

    private int tag;

    private final @NotNull User userProfile;

    public GameUser(@NotNull User userProfile, @NotNull MechanicsTimeService timeService, int it) {
        tag = it;
        this.userProfile = userProfile;
    }


    public @NotNull User getUserProfile() {
        return userProfile;
    }


    public  @NotNull String getUserNickname() {
        return userProfile.getUsername();
    }

    @Override
    public String toString() {
        return "GameUser{"
                + "userProfile=" + userProfile
                + '}';
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
