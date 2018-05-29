package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.mechanics.game.TacticalMap;
import com.gameapi.rha.mechanics.services.ClientTurnService;
import com.gameapi.rha.mechanics.services.GameSessionService;
import com.gameapi.rha.mechanics.services.ResourceFactory;
import com.gameapi.rha.mechanics.services.TurnTimerService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;
    private String playing;
    private TurnTimerService timerService;

    private @NotNull Long  sessionId;
    private @NotNull List<GameUser> players;
    private @NotNull TacticalMap map;

    private  final @NotNull GameSessionService gameSessionService;

    private  final @NotNull ResourceFactory resourceFactory;

    public GameSession(List<GameUser> players, GameSessionService gameSessionService,
                       ResourceFactory resourceFactory, ClientTurnService turnService) {
        this.players = players;
        this.gameSessionService = gameSessionService;
        this.resourceFactory = resourceFactory;
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.playing = players.get(0).getUserNickname();
        this.map = new TacticalMap(resourceFactory.readMap("maps/trainingMap"));

                        Random rand = new Random();
                        switch (players.size()) {
                            case 2:
                                this.map = new TacticalMap(
                                        resourceFactory.readMap(
                                       "maps/2players/map"
                                                 + 3));
                                //   + (Math.abs(rand.nextInt() % 2) + 1)));
                                break;
                            case 3:
                                this.map = new TacticalMap(
                                        resourceFactory.readMap(
                                                "maps/3players/map" + 1

                                        ));
                                //   + (Math.abs(rand.nextInt() % 2) + 1)
                                break;
                            default:
                                this.map = new TacticalMap(resourceFactory.readMap("maps/2players/map" + (rand.nextInt() % 5 + 1)));
                                break;
                        }
                        timerService = new TurnTimerService(turnService, this);

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

    public void tryFinishGame() {
        List<Integer> owners = new ArrayList<>();
        for (List<Hex> ever:map.getMap()) {
            for (Hex one:ever) {
                if (!owners.contains(one.getOwner())) {
                    owners.add(one.getOwner());
                }
            }
        }
        if (owners.size() == 2) {
            if (owners.get(1) != 0) {
                gameSessionService.finishGame(this, owners.get(1));
            }
            gameSessionService.finishGame(this, owners.get(0));
        }
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
                if (hex.getOwner() != 0) {
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
                    if (hex.getUnits() > hex.getMax()) {
                        hex.setUnits(hex.getMax());
                    }
                }

            }
        }
    }

    public String getPlaying() {
        return playing;
    }

    public void setPlaying(String playing) {
        this.playing = playing;
    }

    public TurnTimerService getTimerService() {
        return timerService;
    }

    public void setTimerService(TurnTimerService timerService) {
        this.timerService = timerService;
    }
}
