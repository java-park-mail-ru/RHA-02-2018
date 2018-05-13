package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.models.User;
import com.gameapi.rha.websocket.RemotePointService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import javax.validation.constraints.NotNull;

import java.util.*;


@Service
public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);

    private final @NotNull Map<String, GameSession> usersMap = new HashMap<>();

    private final @NotNull Set<GameSession> gameSessions = new LinkedHashSet<>();


    private final @NotNull RemotePointService remotePointService;


    private final @NotNull MechanicsTimeService timeService;


    private final @NotNull GameInitService gameInitService;


    private final @NotNull GameTaskScheduler gameTaskScheduler;


    private final @NotNull ClientTurnService clientTurnService;

    @NotNull
    private final ResourceFactory resourceFactory;


    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull MechanicsTimeService timeService,
                              @NotNull GameInitService gameInitService,
                              @NotNull GameTaskScheduler gameTaskScheduler,
                              @NotNull ClientTurnService clientTurnService, @NotNull ResourceFactory resourceFactory) {
        this.remotePointService = remotePointService;
        this.timeService = timeService;
        this.gameInitService = gameInitService;
        this.gameTaskScheduler = gameTaskScheduler;
        this.clientTurnService = clientTurnService;
        this.resourceFactory = resourceFactory;
    }

    public Set<GameSession> getSessions() {
        return gameSessions;
    }


    public @Nullable GameSession  getSessionForUser(@NotNull String user) {
        return usersMap.get(user);
    }

    public boolean isPlaying(@NotNull String user) {
        return usersMap.containsKey(user);
    }

    public void forceTerminate(@NotNull GameSession gameSession, boolean error) {
        final boolean exists = gameSessions.remove(gameSession);
        gameSession.setFinished();
        for (GameUser player : gameSession.getPlayers()) {
            usersMap.remove(player.getUserNickname());
         }
        final CloseStatus status = error ? CloseStatus.SERVER_ERROR : CloseStatus.NORMAL;
        if (exists) {
            for (GameUser player : gameSession.getPlayers()) {
                remotePointService.cutDownConnection(player.getUserNickname(), status);
            }
        }

        LOGGER.info("Game session " + gameSession.getSessionId() + (error ? " was terminated due to error. " : " was cleaned. ")
                + gameSession.toString());
    }



    public void startGame(@NotNull List<User> players) {
        List<GameUser> gamers = new ArrayList<>();
        for (User player:players) {
            gamers.add(new GameUser(player, timeService));
        }

        final GameSession gameSession = new GameSession(gamers, resourceFactory);


        gameSessions.add(gameSession);
        for (GameUser gamer: gameSession.getPlayers()) {
            usersMap.put(gamer.getUserNickname(), gameSession);
        }
        gameInitService.initGameFor(gameSession);
        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
    }

    public void finishGame(@NotNull GameSession gameSession) {
        gameSession.setFinished();
        //        final FinishGame.Overcome firstOvercome;
        //        final FinishGame.Overcome secondOvercome;
        //        final int firstScore = gameSession.getFirst().claimPart(MechanicPart.class).getScore();
        //        final int secondScore = gameSession.getSecond().claimPart(MechanicPart.class).getScore();
        //        if (firstScore == secondScore) {
        //            firstOvercome = FinishGame.Overcome.DRAW;
        //            secondOvercome = FinishGame.Overcome.DRAW;
        //        } else if (firstScore > secondScore) {
        //            firstOvercome = FinishGame.Overcome.WIN;
        //            secondOvercome = FinishGame.Overcome.LOSE;
        //        } else {
        //            firstOvercome = FinishGame.Overcome.LOSE;
        //            secondOvercome = FinishGame.Overcome.WIN;
        //        }

        //        try {
        //            remotePointService.sendMessageToUser(gameSession.getFirst().getUsername(), new FinishGame(FinishGame.Overcome.LOSE));
        //        } catch (IOException ex) {
        //            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
        //                    gameSession.getFirst().getUsername()), ex);
        //        }
        //
        //        try {
        //            remotePointService.sendMessageToUser(gameSession.getSecond().getUsername(), new FinishGame(FinishGame.Overcome.LOSE));
        //        } catch (IOException ex) {
        //            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
        //                    gameSession.getSecond().getUsername()), ex);
        //        }
    }

    private static final class SwapTask extends GameTaskScheduler.GameSessionTask {

        private final GameTaskScheduler gameTaskScheduler;
        private final long currentDelay;

        private SwapTask(GameSession gameSession, GameTaskScheduler gameTaskScheduler, long currentDelay) {
            super(gameSession);
            this.gameTaskScheduler = gameTaskScheduler;
            this.currentDelay = currentDelay;
        }

        @Override
        public void operate() {
        }
    }

}

