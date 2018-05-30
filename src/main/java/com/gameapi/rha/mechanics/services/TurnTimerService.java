package com.gameapi.rha.mechanics.services;


import com.gameapi.rha.mechanics.GameSession;

import javax.validation.constraints.NotNull;


public class TurnTimerService implements Runnable {

    @NotNull
    private ClientTurnService clientTurnService;

    private GameSession game;

    private Boolean shouldRun;


    public TurnTimerService(ClientTurnService clientTurnService, GameSession game) {
        this.clientTurnService = clientTurnService;
        this.game = game;
        this.shouldRun = true;
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                System.out.println("Turn interrupted !!");
                break;
            }
            clientTurnService.turn(game);
        }
    }

    public void interrupt() {
        Thread.interrupted();
    }

    public void stop() {
        shouldRun = false;
    }

}
