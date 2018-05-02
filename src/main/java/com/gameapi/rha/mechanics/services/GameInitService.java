package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.game.TacticalMap;
import com.gameapi.rha.mechanics.game.Turn;
import com.gameapi.rha.mechanics.messages.output.InitGame;
import com.gameapi.rha.mechanics.messages.output.ServerTurn;
import com.gameapi.rha.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;


import java.io.IOException;
import java.util.*;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTurnService.class);

    @NotNull
    private final RemotePointService remotePointService;

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            final InitGame.Request initMessage = createInitMessageFor(gameSession);
            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getUserNickname(), initMessage);
            } catch (IOException e) {
                // TODO: Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getUserNickname(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a game", e);
            }
        }
    }

    @SuppressWarnings("TooBroadScope")
    private InitGame.Request createInitMessageFor(@NotNull GameSession gameSession) {
        final InitGame.Request initGameMessage = new InitGame.Request();

        final Map<String, Turn> playerTurns = new HashMap<>();
        final Map<String, String> names = new HashMap<>();
        final TacticalMap map=new TacticalMap();
//        final Map<String, String> colors = new HashMap<>();


//        for (GameUser player : players) {
////            playerSnaps.put(player.getUserNickname(), player.getSnap());
//            names.put(player.getUserNickname(), player.getUserProfile().getEmail());
//        }

//        colors.put(userN, Collectiononfig.SELF_COLOR);
//        colors.put(gameSession.getEnemy(userId).getUserId(), Config.ENEMY_COLOR);

        initGameMessage.setFirst(gameSession.getFirst().getUserNickname());
        initGameMessage.setSecond(gameSession.getSecond().getUserNickname());
        initGameMessage.setMap(map.getMap());
//        initGameMessage.setNames(names);
//        initGameMessage.setColors(colors);
//        initGameMessage.setPlayers(playerSnaps);

//        initGameMessage.setBoard(gameSession.getBoard().getSnap());
        return initGameMessage;
    }
}