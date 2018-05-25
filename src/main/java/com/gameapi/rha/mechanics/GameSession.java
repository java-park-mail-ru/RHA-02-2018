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

        //        Random rand = new Random();
        //        switch (players.size()) {
        //            case 2:
        //                this.map = new TacticalMap(resourceFactory.readMap("maps/2players/map" + (abs(rand.nextInt() % 5) + 1)));
        //                break;
        //            case 3:
        //                this.map = new TacticalMap(resourceFactory.readMap("maps/3players/map" + (rand.nextInt() % 5 + 1)));
        //                break;
        //            case 4:
        //                this.map = new TacticalMap(resourceFactory.readMap("maps/4players/map" + (rand.nextInt() % 5 + 1)));
        //                break;
        //            default:
        //                this.map = new TacticalMap(resourceFactory.readMap("maps/2players/map" + (rand.nextInt() % 5 + 1)));
        //                break;
        //        }

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
            mapTurn();
            return (players.get(0));
        }
    }

    public void mapTurn() {
        for (List<Hex> line: map.getMap()) {
            for (Hex hex: line) {
                switch (hex.getType()) {
                    case 0:
                        break;
                    case 1:
                        hex.setUnits(hex.getUnits() + Config.FIELD_GROWTH);
                        break;
                    case 2:
                        hex.setUnits(hex.getUnits() + Config.DESERT_GROWTH);
                        break;
                    case 3:
                        hex.setUnits(hex.getUnits() + Config.FOREST_GROWTH);
                        break;
                    case 4:
                        hex.setUnits(hex.getUnits() + Config.FIELD_GROWTH);
                        break;
                    case 5:
                        hex.setUnits(hex.getUnits() + Config.FOREST_GROWTH);
                        break;
                    case 6:
                        hex.setUnits(hex.getUnits() + Config.DESERT_GROWTH);
                        break;
                    case 7:
                        hex.setUnits(hex.getUnits() + Config.MOUNTAIN_GROWTH);
                        break;
                    case 8:
                        hex.setUnits(hex.getUnits() + Config.CITY_GROWTH);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
