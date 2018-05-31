package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;

import com.gameapi.rha.mechanics.messages.output.TurnInit;
import com.gameapi.rha.websocket.RemotePointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;


import java.io.IOException;



@Service
public class ClientTurnService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTurnService.class);
    @org.jetbrains.annotations.NotNull
    private final RemotePointService remotePointService;

    public ClientTurnService(@org.jetbrains.annotations.NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void turn(@org.jetbrains.annotations.NotNull GameSession gameSession, String current) {
        gameSession.tryFinishGame();
        String next = gameSession.getNext(current).getUserNickname();
        final TurnInit.Request turnMessage = new TurnInit.Request(next);
        if (next == gameSession.getPlayers().get(0).getUserNickname()) {
            turnMessage.setCycle(true);
        }
        for (GameUser player : gameSession.getPlayers()) {

            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getUserNickname(), turnMessage);
            } catch (IOException e) {
                // TODO: Reentrance mechanism
                gameSession.terminateSession();
                gameSession.getPlayers().forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getUserNickname(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to continue a game", e);
            }
        }
    }
}
