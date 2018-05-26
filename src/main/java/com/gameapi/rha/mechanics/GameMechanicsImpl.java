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
    private final ResourceFactory resourceFactory;


    @NotNull
    private List<ConcurrentLinkedQueue<String>> waiters = new ArrayList<>();



    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);



    public GameMechanicsImpl(@NotNull UserService userService,
                             @NotNull GameInitService gameInitService,
                             @NotNull ClientTurnService clientTurnService,
                             @NotNull ClientStepService clientStepService,
                             @NotNull RemotePointService remotePointService,
                             @NotNull ServerTurnService serverTurnService,
                             @NotNull GameSessionService gameSessionService,
                             @NotNull ResourceFactory resourceFactory) {
        this.userService = userService;
        this.clientStepService = clientStepService;
        this.gameInitService = gameInitService;
        this.clientTurnService = clientTurnService;
        this.serverTurnService = serverTurnService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.resourceFactory = resourceFactory;
        waiters.add(new ConcurrentLinkedQueue<String>());
        waiters.add(new ConcurrentLinkedQueue<String>());
        waiters.add(new ConcurrentLinkedQueue<String>());

    }

    @Override
    public void step(@NotNull String user, @NotNull ClientStep clientStep) {
        GameSession session = gameSessionService.getSessionForUser(user);

        if(session!=null) {
            clientStepService.pushClientStep(session, clientStep);
        }
        else{
            LOGGER.debug(String.format("User %s is not playing",user));
        }
    }

    @Override
    public void addUser(@NotNull String user, Integer players) {
        if (isWaiting(user)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("User %s is playing", user));
            }
            return;
        }
        switch (players) {
            case 2:
                waiters.get(0).add(user);
                break;
            case 3:
                waiters.get(1).add(user);
                break;
            case 4:
                waiters.get(2).add(user);
                break;
            default:
                waiters.get(0).add(user);
                break;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("User %s added to the waiting list", user));
        }
        tryStartGames(players);
    }


    private boolean isWaiting(@NotNull String user) {
        return (waiters.get(0).contains(user) || waiters.get(1).contains(user) || waiters.get(2).contains(user));
    }

    private void tryStartGames(Integer players) {
        final List<User> matchedPlayers = new ArrayList<>();

        while (waiters.get(players - 2).size() + matchedPlayers.size() >= players) {
            final String candidate = waiters.get(players - 2).poll();
            // for sure not null, cause we the only one consumer
            //noinspection ConstantConditions
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(userService.userInfo(candidate));
            if (matchedPlayers.size() >= players) {
                gameSessionService.startGame(matchedPlayers);
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(User::getUsername).forEach(waiters.get(players - 2)::add);
    }

    private boolean insureCandidate(@NotNull String candidate) {
        return remotePointService.isConnected(candidate)
                && userService.userInfo(candidate) != null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void turn(@NotNull  String user, @NotNull ClientTurn clientTurn) {
        clientTurnService.turn(gameSessionService.getSessionForUser(user), user);
    }


}
