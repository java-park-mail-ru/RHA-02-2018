package com.gameapi.rha.mechanics.services;


import com.gameapi.rha.mechanics.GameSession;

import javax.validation.constraints.NotNull;


public class TurnTimerService implements Runnable {

    @NotNull
    private ClientTurnService clientTurnService;

    private GameSession game;

    private Boolean shouldRun;

    private Thread thread;

    public TurnTimerService(ClientTurnService clientTurnService, GameSession game) {
        this.clientTurnService = clientTurnService;
        this.game = game;
        this.shouldRun = true;
        thread = new Thread(this, "Поток-таймер");
        thread.start();
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                Thread.sleep(30000);
                clientTurnService.turn(game);
            } catch (InterruptedException e) {
                System.out.println("Turn interrupted !!");
            }

        }
    }

    public void interrupt() {
        thread.interrupt();
    }

    public void stop() {
        shouldRun = false;
        thread.stop();
    }

}
