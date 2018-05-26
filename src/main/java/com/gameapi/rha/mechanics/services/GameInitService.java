package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.messages.output.InitGame;
import com.gameapi.rha.mechanics.messages.output.TurnInit;
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

    @NotNull
    private final ResourceFactory resourceFactory;

    public GameInitService(@NotNull RemotePointService remotePointService, @NotNull ResourceFactory resourceFactory) {
        this.remotePointService = remotePointService;
        this.resourceFactory = resourceFactory;
    }

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();

        for (GameUser player : gameSession.getPlayers()) {
            final InitGame.Request initMessage = createInitMessageFor(gameSession);
            final TurnInit.Request turnMessage = new TurnInit.Request(gameSession.getPlayers().get(0).getUserNickname());
            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getUserNickname(), initMessage);
                remotePointService.sendMessageToUser(player.getUserNickname(), turnMessage);
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

        final List<String> names = new ArrayList<>();



        for (GameUser player : gameSession.getPlayers()) {
            names.add(player.getUserNickname());
        }


        initGameMessage.setPlayers(names);
        initGameMessage.setMap(gameSession.getMap().getMap());
        return initGameMessage;
    }
}