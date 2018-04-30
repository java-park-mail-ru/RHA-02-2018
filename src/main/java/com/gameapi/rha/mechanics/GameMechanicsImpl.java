package com.gameapi.rha.mechanics;

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

//    @NotNull
//    private final GameInitService gameInitService;

//    @NotNull
//    private final ClientTurnService clientTurnService;

    @NotNull
    private final RemotePointService remotePointService;

//    @NotNull
//    private final ServerTurnService serverTurnService;

    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final MechanicsTimeService timeService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;
//
//    @NotNull
//    private ConcurrentLinkedQueue<Id<UserProfile>> waiters = new ConcurrentLinkedQueue<>();
//
//    @NotNull
//    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    @NotNull
    private ConcurrentLinkedQueue<String> waiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);


    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanicsImpl(@NotNull UserService userService,
//                             @NotNull GameInitService gameInitService,
//                             @NotNull ClientTurnService serverSnapshotService,
//                             @NotNull ClientTurnService clientTurnService,
                             @NotNull RemotePointService remotePointService,
//                             @NotNull ServerTurnService serverTurnService,
                             @NotNull GameSessionService gameSessionService,
                             @NotNull MechanicsTimeService timeService,
                             @NotNull GameTaskScheduler gameTaskScheduler) {
        this.userService = userService;
//        this.gameInitService = gameInitService;
//        this.clientTurnService = clientTurnService;
//        this.serverTurnService = serverTurnService;


        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.timeService = timeService;
        this.gameTaskScheduler = gameTaskScheduler;
    }

    @Override
    public void Turn(@NotNull String user, @NotNull ClientTurn clientSnap) {
//        tasks.add(() -> clientTurnService.pushClientTurn(user, clientSnap));
    }

    @Override
    public void addUser(@NotNull String user) {
//        if (gameSessionService.isPlaying(user)) {
//            return;
//        }
        waiters.add(user);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("User %s added to the waiting list", user));
        }
    }

    private void tryStartGames() {
        final Set<User> matchedPlayers = new LinkedHashSet<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final String candidate = waiters.poll();
            // for sure not null, cause we the only one consumer
            //noinspection ConstantConditions
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(userService.userInfo(candidate));
            if (matchedPlayers.size() == 2) {
                final Iterator<User> iterator = matchedPlayers.iterator();
//                gameSessionService.startGame(iterator.next(), iterator.next());
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
    public void gmStep(long frameTime) {

        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Can't handle game task", ex);
                }
            }
        }

//        for (GameSession session : gameSessionService.getSessions()) {
//            clientTurnService.processSnapshotsFor(session);
//        }

        gameTaskScheduler.tick();

        final List<GameSession> sessionsToTerminate = new ArrayList<>();
        final List<GameSession> sessionsToFinish = new ArrayList<>();
//        for (GameSession session : gameSessionService.getSessions()) {
//            if (session.tryFinishGame()) {
//                sessionsToFinish.add(session);
//                continue;
//            }
//

            try {
//                serverSnapshotService.sendSnapshotsFor(session, frameTime);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
//                sessionsToTerminate.add(session);
            }
//            pullTheTriggerService.pullTheTriggers(session);
//        }
//        sessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
//        sessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));

        tryStartGames();
//        clientSnapshotsService.reset();
        timeService.tick(frameTime);
    }

    @Override
    public void reset() {

    }

//    @NotNull
//    private final AccountService accountService;
//
//    @NotNull
//    private final ClientSnapshotsService clientSnapshotsService;
//
//    @NotNull
//    private final ServerSnapshotService serverSnapshotService;
//
//    @NotNull
//    private final RemotePointService remotePointService;
//
//    @NotNull
//    private final PullTheTriggerService pullTheTriggerService;
//
//    @NotNull
//    private final GameSessionService gameSessionService;
//
//    @NotNull
//    private final MechanicsTimeService timeService;
//
//    @NotNull
//    private final GameTaskScheduler gameTaskScheduler;
//
//    @NotNull
//    private ConcurrentLinkedQueue<Id<UserProfile>> waiters = new ConcurrentLinkedQueue<>();
//
//    @NotNull
//    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
//
//    public GameMechanicsImpl(@NotNull AccountService accountService,
//                             @NotNull ClientSnapshotsService clientSnapshotsService,
//                             @NotNull ServerSnapshotService serverSnapshotService,
//                             @NotNull RemotePointService remotePointService,
//                             @NotNull PullTheTriggerService pullTheTriggerService,
//                             @NotNull GameSessionService gameSessionService,
//                             @NotNull MechanicsTimeService timeService,
//                             @NotNull GameTaskScheduler gameTaskScheduler) {
//        this.accountService = accountService;
//        this.clientSnapshotsService = clientSnapshotsService;
//        this.serverSnapshotService = serverSnapshotService;
//        this.remotePointService = remotePointService;
//        this.pullTheTriggerService = pullTheTriggerService;
//        this.gameSessionService = gameSessionService;
//        this.timeService = timeService;
//        this.gameTaskScheduler = gameTaskScheduler;
//    }
//
//    @Override
//    public void addClientSnapshot(@NotNull Id<UserProfile> userId, @NotNull ClientSnap clientSnap) {
//        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, clientSnap));
//    }
//
//    @Override
//    public void addUser(@NotNull Id<UserProfile> userId) {
//        if (gameSessionService.isPlaying(userId)) {
//            return;
//        }
//        waiters.add(userId);
//        if (LOGGER.isDebugEnabled()) {
//            final UserProfile user = accountService.getUserById(userId);
//            LOGGER.debug(String.format("User %s added to the waiting list", user.getLogin()));
//        }
//    }
//
//    private void tryStartGames() {
//        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();
//
//        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
//            final Id<UserProfile> candidate = waiters.poll();
//            // for sure not null, cause we the only one consumer
//            //noinspection ConstantConditions
//            if (!insureCandidate(candidate)) {
//                continue;
//            }
//            matchedPlayers.add(accountService.getUserById(candidate));
//            if (matchedPlayers.size() == 2) {
//                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
//                gameSessionService.startGame(iterator.next(), iterator.next());
//                matchedPlayers.clear();
//            }
//        }
//        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add);
//    }
//
//    private boolean insureCandidate(@NotNull Id<UserProfile> candidate) {
//        return remotePointService.isConnected(candidate)
//                && accountService.getUserById(candidate) != null;
//    }
//
//    @Override
//    public void gmStep(long frameTime) {
//        while (!tasks.isEmpty()) {
//            final Runnable nextTask = tasks.poll();
//            if (nextTask != null) {
//                try {
//                    nextTask.run();
//                } catch (RuntimeException ex) {
//                    LOGGER.error("Can't handle game task", ex);
//                }
//            }
//        }
//
//        for (GameSession session : gameSessionService.getSessions()) {
//            clientSnapshotsService.processSnapshotsFor(session);
//        }
//
//        gameTaskScheduler.tick();
//
//        final List<GameSession> sessionsToTerminate = new ArrayList<>();
//        final List<GameSession> sessionsToFinish = new ArrayList<>();
//        for (GameSession session : gameSessionService.getSessions()) {
//            if (session.tryFinishGame()) {
//                sessionsToFinish.add(session);
//                continue;
//            }
//
//            if (!gameSessionService.checkHealthState(session)) {
//                sessionsToTerminate.add(session);
//                continue;
//            }
//
//            try {
//                serverSnapshotService.sendSnapshotsFor(session, frameTime);
//            } catch (RuntimeException ex) {
//                LOGGER.error("Failed to send snapshots, terminating the session", ex);
//                sessionsToTerminate.add(session);
//            }
//            pullTheTriggerService.pullTheTriggers(session);
//        }
//        sessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));
//        sessionsToFinish.forEach(session -> gameSessionService.forceTerminate(session, false));
//
//        tryStartGames();
//        clientSnapshotsService.reset();
//        timeService.tick(frameTime);
//    }
//
//    @Override
//    public void reset() {
//        for (GameSession session : gameSessionService.getSessions()) {
//            gameSessionService.forceTerminate(session, true);
//        }
//        waiters.forEach(user -> remotePointService.cutDownConnection(user, CloseStatus.SERVER_ERROR));
//        waiters.clear();
//        tasks.clear();
//        clientSnapshotsService.reset();
//        timeService.reset();
//        gameTaskScheduler.reset();
//    }
}
