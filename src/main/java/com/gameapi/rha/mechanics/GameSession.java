package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.game.TacticalMap;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;
    @NotNull
    private Long sessionId;

    @NotNull
    private GameUser first;
    @NotNull
    private GameUser second;
    @NotNull
    private TacticalMap map;

    public GameSession(@NotNull GameUser first, @NotNull GameUser second) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.first = first;
        this.second = second;
        this.map=new TacticalMap();
    }


    public void terminateSession() {
    }

    public GameUser getFirst() {
        return first;
    }

    public GameUser getSecond() {
        return second;
    }

    public void setFinished() {
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Object getEnemy(@org.jetbrains.annotations.NotNull String userN) {
        return userN;
    }

    public TacticalMap getMap() {
        return map;
    }

    public void setMap(TacticalMap map) {
        this.map = map;
    }
}
