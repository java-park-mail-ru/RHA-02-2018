package com.gameapi.rha.mechanics.messages.outbox;

import com.gameapi.rha.mechanics.game.GameBoard;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.models.User;
import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class InitGame {
    public static final class Request extends Message {
        private User self;
        private User enemy;
        private GameBoard.BoardSnap board;
        private Map<User, User.ServerPlayerSnap> players;
        private Map<User, String> names;
        private Map<User, String> colors;

        public GameBoard.BoardSnap getBoard() {
            return board;
        }

        public void setBoard(Board.BoardSnap board) {
            this.board = board;
        }

        @NotNull
        public Map<User, String> getNames() {
            return names;
        }

        public void setNames(@NotNull Map<User, String> names) {
            this.names = names;
        }

        @NotNull
        public User getSelf() {
            return self;
        }

        public User getEnemy() {
            return enemy;
        }

        public void setEnemy(User enemy) {
            this.enemy = enemy;
        }

        @NotNull
        public Map<User, String> getColors() {
            return colors;
        }

        public void setColors(@NotNull Map<User, String> colors) {
            this.colors = colors;
        }

        public void setSelf(@NotNull User self) {
            this.self = self;
        }

        @NotNull
        public Map<User, GameUser.ServerPlayerSnap> getPlayers() {
            return players;
        }

        public void setPlayers(@NotNull Map<User, GameUser.ServerPlayerSnap> players) {
            this.players = players;
        }
    }

}

