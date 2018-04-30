package com.gameapi.rha.mechanics;

import com.gameapi.rha.mechanics.game.GameBoard;
import com.gameapi.rha.models.User;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private boolean isFinished;

    @NotNull
    private final Integer sessionId;
    @NotNull
    private final User first;
    @NotNull
    private final User second;
    @NotNull
    private final GameBoard board;
    @NotNull
    private final GameSessionService gameSessionService;

    public GameSession(@NotNull User user1,
                       @NotNull User user2,
                       @NotNull GameSessionService gameSessionService,
                       @NotNull MechanicsTimeService mechanicsTimeService,
                       @NotNull Shuffler shuffler) {
        this.gameSessionService = gameSessionService;
        this.sessionId = Math.toIntExact(ID_GENERATOR.getAndIncrement());
        this.first = user1;
        this.second = user2;
        this.board = new GameBoard(this, shuffler);
        this.isFinished = false;
    }

    @NotNull
    public Integer getSessionId() {
        return sessionId;
    }

    @NotNull
    public GameBoard getGameBoard() {
        return board;
    }

    @NotNull
    public User getEnemy(@NotNull User userId) {
        if (userId.getEmail().equals(first.getEmail())) {
            return second;
        }
        if (userId.getEmail().equals(second.getEmail())) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for game but user not participant");
    }

    @NotNull
    public User getFirst() {
        return first;
    }

    @NotNull
    public User getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object subj) {
        if (this == subj) {
            return true;
        }
        if (subj == null || getClass() != subj.getClass()) {
            return false;
        }

        final GameSession another = (GameSession) subj;

        return sessionId.equals(another.sessionId);
    }

    @NotNull
    public List<User> getPlayers() {
        return Arrays.asList(first, second);
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

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return '['
                + "sessionId=" + sessionId
                + ", first=" + first
                + ", second=" + second
                + ']';
    }

    public boolean tryFinishGame() {
        if (first.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN
                || second.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN) {
            gameSessionService.finishGame(this);
            isFinished = true;
            return true;
        }
        return false;
    }
}
