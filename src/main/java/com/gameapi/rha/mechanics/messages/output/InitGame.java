package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class InitGame {
    public static final class Request extends Message {
        private String first;
        private String second;
        private List<Hex> map;





        @NotNull
        public List<Hex> getMap() {
            return map;
        }

        public void setMap(@NotNull List<Hex> names) {
            this.map = names;
        }

        @NotNull
        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(@NotNull String enemy) {
            this.second = enemy;
        }



        public void setFirst(@NotNull String self) {
            this.first = self;
        }


    }

}
