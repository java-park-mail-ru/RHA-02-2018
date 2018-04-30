package com.gameapi.rha.mechanics.services;
//
//import com.gameapi.rha.mechanics.Config;
//import com.gameapi.rha.mechanics.GameSession;
//import com.gameapi.rha.mechanics.game.GameUser;
//import com.gameapi.rha.mechanics.game.MechanicPart;
//import com.gameapi.rha.mechanics.messages.outbox.FinishGame;
//import com.gameapi.rha.models.User;
//import com.gameapi.rha.websocket.RemotePointService;
//import org.jetbrains.annotations.Nullable;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
//import org.springframework.web.socket.CloseStatus;
//
//import javax.validation.constraints.NotNull;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
//

@Service
public class GameSessionService {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);
//    @NotNull
//    private final Map<String, GameSession> usersMap = new HashMap<>();
//    @NotNull
//    private final Set<GameSession> gameSessions = new LinkedHashSet<>();
//
//    @NotNull
//    private final RemotePointService remotePointService;
//
//    @NotNull
//    private final MechanicsTimeService timeService;
//
//    @NotNull
//    private final GameInitService gameInitService;
//
//    @NotNull
//    private final GameTaskScheduler gameTaskScheduler;
//
//    @NotNull
//    private final ClientTurnService clientSnapshotsService;
//
//
//    public GameSessionService(@NotNull RemotePointService remotePointService,
//                              @NotNull MechanicsTimeService timeService,
//                              @NotNull GameInitService gameInitService,
//                              @NotNull GameTaskScheduler gameTaskScheduler,
//                              @NotNull ClientTurnService clientSnapshotsService) {
//        this.remotePointService = remotePointService;
//        this.timeService = timeService;
//        this.gameInitService = gameInitService;
//        this.gameTaskScheduler = gameTaskScheduler;
//        this.clientSnapshotsService = clientSnapshotsService;
//    }
//
//    public Set<GameSession> getSessions() {
//        return gameSessions;
//    }
//
//    @Nullable
//    public GameSession getSessionForUser(@NotNull String user) {
//        return usersMap.get(user);
//    }
//
//    public boolean isPlaying(@NotNull String user) {
//        return usersMap.containsKey(user);
//    }
//
//    public void forceTerminate(@NotNull GameSession gameSession, boolean error) {
//        final boolean exists = gameSessions.remove(gameSession);
//        gameSession.setFinished();
//        usersMap.remove(gameSession.getFirst().getUsername());
//        usersMap.remove(gameSession.getSecond().getUsername());
//        final CloseStatus status = error ? CloseStatus.SERVER_ERROR : CloseStatus.NORMAL;
//        if (exists) {
//            remotePointService.cutDownConnection(gameSession.getFirst().getUsername(), status);
//            remotePointService.cutDownConnection(gameSession.getSecond().getUsername(), status);
//        }
//        clientSnapshotsService.clearForUser(gameSession.getFirst().getUsername());
//        clientSnapshotsService.clearForUser(gameSession.getSecond().getUsername());
//
//        LOGGER.info("Game session " + gameSession.getSessionId() + (error ? " was terminated due to error. " : " was cleaned. ")
//                + gameSession.toString());
//    }
//
////    public boolean checkHealthState(@NotNull GameSession gameSession) {
////        return gameSession.getPlayers().stream().map(GameUser::getUserId).allMatch(remotePointService::isConnected);
////    }
//
//    public void startGame(@NotNull User first, @NotNull User second) {
//        final GameSession gameSession = new GameSession(first, second, this, timeService);
//        gameSessions.add(gameSession);
//        usersMap.put(gameSession.getFirst().getUsername(), gameSession);
//        usersMap.put(gameSession.getSecond().getUsername(), gameSession);
////        gameSession.getBoard().randomSwap();
////        gameInitService.initGameFor(gameSession);
//        gameTaskScheduler.schedule(Config.SWITCH_DELAY, new SwapTask(gameSession, gameTaskScheduler, Config.SWITCH_DELAY));
//        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
//    }
//
//    public void finishGame(@NotNull GameSession gameSession) {
//        gameSession.setFinished();
//        final FinishGame.Overcome firstOvercome;
//        final FinishGame.Overcome secondOvercome;
////        final int firstScore = gameSession.getFirst().claimPart(MechanicPart.class).getScore();
////        final int secondScore = gameSession.getSecond().claimPart(MechanicPart.class).getScore();
////        if (firstScore == secondScore) {
////            firstOvercome = FinishGame.Overcome.DRAW;
////            secondOvercome = FinishGame.Overcome.DRAW;
////        } else if (firstScore > secondScore) {
////            firstOvercome = FinishGame.Overcome.WIN;
////            secondOvercome = FinishGame.Overcome.LOSE;
////        } else {
////            firstOvercome = FinishGame.Overcome.LOSE;
////            secondOvercome = FinishGame.Overcome.WIN;
////        }
//
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
//    }
//
//    private static final class SwapTask extends GameTaskScheduler.GameSessionTask {
//
//        private final GameTaskScheduler gameTaskScheduler;
//        private final long currentDelay;
//
//        private SwapTask(GameSession gameSession, GameTaskScheduler gameTaskScheduler, long currentDelay) {
//            super(gameSession);
//            this.gameTaskScheduler = gameTaskScheduler;
//            this.currentDelay = currentDelay;
//        }
//
//        @Override
//        public void operate() {
//            if (getGameSession().isFinished()) {
//                return;
//            }
//            final long newDelay = Math.max(currentDelay - Config.SWITCH_DELAY, Config.SWITCH_DELAY);
//            gameTaskScheduler.schedule(newDelay,
//                    new SwapTask(getGameSession(), gameTaskScheduler, newDelay));
//        }
//    }
//
}

