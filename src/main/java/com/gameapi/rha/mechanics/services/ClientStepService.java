package com.gameapi.rha.mechanics.services;


import com.gameapi.rha.mechanics.GameSession;
import com.gameapi.rha.mechanics.game.GameUser;
import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.mechanics.messages.input.ClientStep;
import com.gameapi.rha.mechanics.messages.output.ServerStep;
import com.gameapi.rha.websocket.RemotePointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.jetbrains.annotations.NotNull;
import com.gameapi.rha.mechanics.Config;
import org.springframework.web.socket.CloseStatus;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: Допилить степ

@Service
public class ClientStepService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTurnService.class);

    @NotNull
    private final RemotePointService remotePointService;

    public ClientStepService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }



    public void pushClientStep(@NotNull GameSession gameSession, @NotNull ClientStep clientStep) {
        Hex fromHex = gameSession.getMap()
                .get(clientStep.getFrom().get(0), clientStep.getFrom().get(1));

        Hex toHex = gameSession.getMap()
                .get(clientStep.getTo().get(0), clientStep.getTo().get(1));

        List<Hex> changes = new ArrayList<>();
        String type;

        if (fromHex.getOwner().equals(toHex.getOwner())) {
            type = "move";
            move(toHex, fromHex);
        } else {
            type = "attack";

            double victoryProbability = Math.atan((double) (fromHex.getUnits())
                    * Config.MOVING_UNITS_COEFF
                    / (double) toHex.getUnits() / Config.CASUALTIES_COEFF) /  Math.PI * 2;
            final Random rand = new Random();
            final Double randomToken = Math.abs((double) rand.nextInt() % 100);

            if (victoryProbability * 100 > randomToken) {
                attackV(toHex, fromHex, gameSession, changes, rand);

            } else {
                attackL(toHex, fromHex, rand);
            }
        }

        gameSession.getMap().getMap().get(clientStep.getTo().get(0))
                .set(clientStep.getTo().get(1), toHex);
        changes.add(toHex);
        gameSession.getMap().getMap().get(clientStep.getFrom().get(0))
                .set(clientStep.getFrom().get(1), fromHex);

        changes.add(fromHex);
        for (GameUser player : gameSession.getPlayers()) {
            final ServerStep stepMessage = createServerStepMessage(gameSession, changes, type);
            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getUserNickname(), stepMessage);
            } catch (IOException e) {
                // TODO: Reentrance mechanism
                gameSession.getPlayers().forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getUserNickname(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to make a step", e);
            }
        }
    }




    private ServerStep createServerStepMessage(@NotNull GameSession gameSession, List<Hex> changes, String type) {
        final ServerStep serverStepMessage = new ServerStep();
        serverStepMessage.setMap(changes);
        serverStepMessage.setType(type);
        return serverStepMessage;
    }







    private void move(Hex toHex, Hex fromHex) {
        toHex.setUnits(toHex.getUnits()
                + (int) (((double) fromHex.getUnits()) * Config.MOVING_UNITS_COEFF));

        fromHex.setUnits((int) (((double) fromHex.getUnits())
                * (1 - Config.MOVING_UNITS_COEFF)));
    }
    //TODO: Fix attackLost



    private void attackV(Hex toHex, Hex fromHex, GameSession gameSession,
                         List<Hex> changes, Random rand) {
        for (List<Integer> retreatHex:toHex.getNeighbours()) {
            if (gameSession.getMap().get(retreatHex.get(0), retreatHex.get(1))
                    .getOwner().equals(toHex.getOwner())) {
                gameSession.getMap().get(retreatHex.get(0), retreatHex.get(1)).setUnits(
                        gameSession.getMap().get(retreatHex.get(0), retreatHex.get(1)).getUnits()
                                + (toHex.getUnits() * (rand.nextInt() % Config.RETREATED_LOST_TROOPS_MAX + 10) / 100));
                changes.add(gameSession.getMap().get(retreatHex.get(0), retreatHex.get(1)));
                break;
            }
        }
        toHex.setOwner(fromHex.getOwner());
        if (fromHex.getUnits() * Config.MOVING_UNITS_COEFF > toHex.getUnits() * Config.CASUALTIES_COEFF) {
            toHex.setUnits((int) (fromHex.getUnits() * Config.MOVING_UNITS_COEFF
                    - toHex.getUnits() * Config.CASUALTIES_COEFF));
        } else {
            toHex.setUnits(0);
        }
        toHex.setUnits((int) (toHex.getUnits() + ((fromHex.getUnits()
                * Config.MOVING_UNITS_COEFF) - toHex.getUnits())
                / 100 * (rand.nextInt() % Config.RETREATED_VICTORIOUS_TROOPS_MAX + 20)));
        fromHex.setUnits((int) (fromHex.getUnits() * (1 - Config.MOVING_UNITS_COEFF)));
    }



    private void attackL(Hex toHex, Hex fromHex, Random rand) {
        if (fromHex.getUnits() * Config.MOVING_UNITS_COEFF < toHex.getUnits() * Config.CASUALTIES_COEFF) {
            Integer fighRes = ((int) (toHex.getUnits() * Config.CASUALTIES_COEFF
                    - fromHex.getUnits() * Config.MOVING_UNITS_COEFF));
            toHex.setUnits(fighRes + (toHex.getUnits() - fighRes)
                    / 100 * (rand.nextInt() % Config.RETREATED_VICTORIOUS_TROOPS_MAX + 20));
        } else {
            toHex.setUnits(toHex.getUnits() / 100 * (rand.nextInt()
                    % Config.RETREATED_VICTORIOUS_TROOPS_MAX + 20));
        }
        fromHex.setUnits((int) (fromHex.getUnits() * (1 - Config.MOVING_UNITS_COEFF)
                + fromHex.getUnits() * Config.MOVING_UNITS_COEFF * (rand.nextInt() % Config.RETREATED_LOST_TROOPS_MAX + 10) / 100));
    }
}
