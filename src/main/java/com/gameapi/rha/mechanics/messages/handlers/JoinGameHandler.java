package com.gameapi.rha.mechanics.messages.handlers;

import com.gameapi.rha.mechanics.GameMechanics;
import com.gameapi.rha.mechanics.messages.input.JoinGame;
import com.gameapi.rha.websocket.MessageHandler;
import com.gameapi.rha.websocket.MessageHandlerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class JoinGameHandler extends MessageHandler<JoinGame> {
      @NotNull
      private final GameMechanics gameMechanics;
      @NotNull
      private final MessageHandlerContainer messageHandlerContainer;

      public JoinGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
          super(JoinGame.class);
          this.gameMechanics = gameMechanics;
          this.messageHandlerContainer = messageHandlerContainer;
        }

      @PostConstruct
      private void init() {
          messageHandlerContainer.registerHandler(JoinGame.class, this);
      }

      @Override
      public void handle(@NotNull JoinGame message, @NotNull String user) {
          gameMechanics.addUser(user, message.getPlayers());

      }
}

