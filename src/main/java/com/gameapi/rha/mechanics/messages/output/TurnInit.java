package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.websocket.Message;

public class TurnInit {

        public static class Request extends Message {
                private String user;
                private boolean cycle;

                public Request(String user) {
                        this.user = user;
                        this.cycle = false;
                }

                public String getUser() {
                        return user;
                }

                public boolean isCycle() {
                        return cycle;
                }

                public void setCycle(boolean cycle) {
                        this.cycle = cycle;
                }
        }
}
