package com.gameapi.rha.mechanics.messages.output;

import com.gameapi.rha.websocket.Message;

public class TurnInit {

        public static class Request extends Message {
                private String user;

                public Request(String user) {
                        this.user = user;
                }
        }
}
