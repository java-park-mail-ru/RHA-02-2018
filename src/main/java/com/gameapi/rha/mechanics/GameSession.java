package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.mechanics.game.TacticalMap;
import com.gameapi.rha.mechanics.services.GameSessionService;
import com.gameapi.rha.mechanics.services.ResourceFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;

    private @NotNull Long  sessionId;
    private @NotNull List<GameUser> players;
    private @NotNull TacticalMap map;

    private  final @NotNull GameSessionService gameSessionService;

    private  final @NotNull ResourceFactory resourceFactory;

    public GameSession(List<GameUser> players, GameSessionService gameSessionService, ResourceFactory resourceFactory) {
        this.players = players;
        this.gameSessionService = gameSessionService;
        this.resourceFactory = resourceFactory;
        this.sessionId = ID_GENERATOR.getAndIncrement();

        this.map = new TacticalMap(resourceFactory.readMap("maps/trainingMap"));
    }




    public List<GameUser> getPlayers() {
        return players;
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

    public boolean tryFinishGame() {
        Hex check = map.getMap().get(0).get(0);
        for (List<Hex> ever:map.getMap()) {
            for (Hex one:ever) {
            if (check.getOwner() != one.getOwner()) {
                return false;
            }
        }
        }
        gameSessionService.finishGame(this);
        return true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

    public void terminateSession() {
        gameSessionService.forceTerminate(this, true);
    }

    public GameUser getNext(String current) {
        Integer i = 0;
        while (!(players.get(i).getUserNickname().equals(current)) && i < players.size()) {
            i++;
        }
        if (i + 1 < players.size()) {
            return (players.get(i + 1));
        } else {
            return (players.get(0));
        }
    }
}
