package com.gameapi.rha.mechanics.game;

import com.gameapi.rha.mechanics.services.MechanicsTimeService;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;

public class GameUser extends GameObject {
    @NotNull
    private final User userProfile;

    public GameUser(@NotNull User userProfile, @NotNull MechanicsTimeService timeService) {
        this.userProfile = userProfile;
//        addPart(MechanicPart.class, new MechanicPart(timeService));
    }

    @NotNull
    public User getUserProfile() {
        return userProfile;
    }

    @NotNull
    public String getUserNickname() {
        return userProfile.getUsername();
    }

    @Override
    public String toString() {
        return "GameUser{"
                + "userProfile=" + userProfile
                + '}';
    }
}
