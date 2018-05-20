package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

public class InitGame {
    public static final class Request extends Message {
        private List<String> players;
        private List<List<Hex>> map;



        public @NotNull List<List<Hex>> getMap() {
            return map;
        }

        public void setMap(@NotNull List<List<Hex>>  names) {
            this.map = names;
        }


        public List<String> getPlayers() {
            return players;
        }

        public void setPlayers(List<String> enemies) {
            this.players = enemies;
        }
    }

}
