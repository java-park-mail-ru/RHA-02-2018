package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.mechanics.services.*;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import com.gameapi.rha.websocket.RemotePointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanicsImpl implements GameMechanics {

    @NotNull
    private final UserService userService;

    @NotNull
    private final GameInitService gameInitService;

    @NotNull
    private final ClientStepService clientStepService;

    @NotNull
    private final ClientTurnService clientTurnService;

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final ServerTurnService serverTurnService;

    @NotNull
    private final GameSessionService gameSessionService;


    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private ConcurrentLinkedQueue<String> waiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);


//    @NotNull
//    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanicsImpl(@NotNull UserService userService,
                             @NotNull GameInitService gameInitService,
                             @NotNull ClientTurnService clientTurnService,
                             @NotNull ClientStepService clientStepService,
                             @NotNull RemotePointService remotePointService,
                             @NotNull ServerTurnService serverTurnService,
                             @NotNull GameSessionService gameSessionService,
                             @NotNull GameTaskScheduler gameTaskScheduler) {
        this.userService = userService;
        this.clientStepService = clientStepService;
        this.gameInitService = gameInitService;
        this.clientTurnService = clientTurnService;
        this.serverTurnService = serverTurnService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.gameTaskScheduler = gameTaskScheduler;
    }

    @Override
    public void step(@NotNull String user, @NotNull ClientStep clientStep) {

//        tasks.add(() -> clientStepService.pushClientStep(gameSessionService.getSessionForUser(user), clientStep));
        clientStepService.pushClientStep( gameSessionService.getSessionForUser(user), clientStep);
    }

    @Override
    public void addUser(@NotNull String user) {
        if (gameSessionService.isPlaying(user)) {
            return;
        }
        waiters.add(user);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("User %s added to the waiting list", user));
        }
        tryStartGames();
    }

    private void tryStartGames() {
        final List<User> matchedPlayers = new ArrayList<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final String candidate = waiters.poll();
            // for sure not null, cause we the only one consumer
            //noinspection ConstantConditions
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(userService.userInfo(candidate));
            if (matchedPlayers.size() >= 2) {
                gameSessionService.startGame(matchedPlayers);
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(User::getUsername).forEach(waiters::add);
    }

    private boolean insureCandidate(@NotNull String candidate) {
        return remotePointService.isConnected(candidate)
                && userService.userInfo(candidate) != null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void turn(@NotNull  String user, @NotNull ClientTurn clientTurn){

    };

}
