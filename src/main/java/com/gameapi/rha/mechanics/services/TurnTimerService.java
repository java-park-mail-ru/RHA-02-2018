package com.gameapi.rha.mechanics.services;

import javax.validation.constraints.NotNull;


public class TurnTimerService implements Runnable {

    @NotNull
    private GameSessionService gameSessionService;

    private Boolean shouldRun;

    private Thread thread;

    public TurnTimerService(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
        this.shouldRun = true;
        thread = new Thread(this, "Поток-таймер");
        thread.start();
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                gameSessionService.tryEndTurn();
                Thread.sleep(300);
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
