package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.messages.output.FinishGame;
import com.gameapi.rha.mechanics.messages.output.TurnInit;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import com.gameapi.rha.websocket.RemotePointService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.*;


@Service
public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);

    @Autowired
    private UserService us;

    private final @NotNull Map<String, GameSession> usersMap = new HashMap<>();

    private final @NotNull Set<GameSession> gameSessions = new LinkedHashSet<>();

    private final int turnTime = 30000;

    private final @NotNull RemotePointService remotePointService;


    private final @NotNull MechanicsTimeService timeService;


    private final @NotNull GameInitService gameInitService;


    private final @NotNull GameTaskScheduler gameTaskScheduler;


    private final @NotNull ClientTurnService clientTurnService;


    @NotNull
    private final ResourceFactory resourceFactory;


    private final TurnTimerService timerService;

    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull MechanicsTimeService timeService,
                              @NotNull GameInitService gameInitService,
                              @NotNull GameTaskScheduler gameTaskScheduler,
                              @NotNull ClientTurnService clientTurnService,
                              @NotNull ResourceFactory resourceFactory) {
        this.remotePointService = remotePointService;
        this.timeService = timeService;
        this.gameInitService = gameInitService;
        this.gameTaskScheduler = gameTaskScheduler;
        this.clientTurnService = clientTurnService;
        this.resourceFactory = resourceFactory;
        timerService = new TurnTimerService(this);
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


    public void tryEndTurn() {
        for (GameSession session : gameSessions) {
            if (System.currentTimeMillis() - session.getLastTurn() > turnTime) {
                clientTurnService.turn(session);
            }
        }
    }

    public void startGame(@NotNull List<User> players) {
        List<GameUser> gamers = new ArrayList<>();
        int i = 1;
        for (User player:players) {
            gamers.add(new GameUser(player, timeService, i));
            i++;
        }

        final GameSession gameSession = new GameSession(gamers, this, resourceFactory, clientTurnService);


        gameSessions.add(gameSession);
        for (GameUser gamer: gameSession.getPlayers()) {
            usersMap.put(gamer.getUserNickname(), gameSession);
        }
        gameInitService.initGameFor(gameSession);
        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
    }

    public void finishGame(@NotNull GameSession gameSession, @NotNull Integer player) {
        if (gameSession != null) {

            FinishGame msg = new FinishGame();
            if (player != 0) {
                msg.setPlayer(gameSession.getPlayers().get(player - 1).getTag());
                us.addRating(gameSession.getPlayers().get(player - 1).getUserNickname());
            }
            //            gameSession.getTimerService().stop();
            //            gameSession.getTimerService().interrupt();
            for (GameUser user : gameSession.getPlayers()) {
                usersMap.remove(user.getUserNickname());
                try {
                    remotePointService.sendMessageToUser(user.getUserNickname(), msg);
                    remotePointService.removeUser(user.getUserNickname());
                } catch (IOException exc) {
                    LOGGER.warn("Cannot finish gameSession");
                }

            }
            gameSessions.remove(gameSession);
        }
    }

    public void dropPlayer(@Nullable GameSession sessionForUser, String user) {

            if (sessionForUser != null) {
                if (sessionForUser.getPlayers().size() - 1 > 1) {
                    if (sessionForUser.getPlaying().equals(user)) {

                        sessionForUser.updateLastTurn();
                        String next = sessionForUser.getNext(sessionForUser.getPlaying()).getUserNickname();
                        sessionForUser.setPlaying(next);
                        sessionForUser.getPlayers().remove(sessionForUser.getPlayer(user));
                        final TurnInit.Request turnMessage = new TurnInit.Request(next);
                        if (next == sessionForUser.getPlayers().get(0).getUserNickname()) {
                            turnMessage.setCycle(true);
                        }
                        for (GameUser player : sessionForUser.getPlayers()) {

                            //noinspection OverlyBroadCatchBlock
                            try {
                                remotePointService.sendMessageToUser(player.getUserNickname(), turnMessage);
                            } catch (IOException e) {
                                // TODO: Reentrance mechanism
                                sessionForUser.terminateSession();
                                sessionForUser.getPlayers().forEach(playerToCutOff ->
                                        remotePointService.cutDownConnection(playerToCutOff.getUserNickname(),
                                        CloseStatus.SERVER_ERROR));
                                LOGGER.error("Unnable to continue a game", e);
                            }
                        }
                        usersMap.remove(user);
                    }


                } else {
                    if (sessionForUser.getPlayers().get(0).getUserNickname().equals(user)) {
                        sessionForUser.getPlayers().remove(sessionForUser.getPlayers().get(0));
                        finishGame(sessionForUser, 1);

                    } else {
                        sessionForUser.getPlayers().remove(sessionForUser.getPlayers().get(1));
                        finishGame(sessionForUser, 1);
                    }
                }
            }
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

