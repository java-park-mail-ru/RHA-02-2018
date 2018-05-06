package com.gameapi.rha.mechanics.messages.input;

import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;

public class ClientStep extends Message {


    private @NotNull Integer from;

    private @NotNull Integer to;

    public ClientStep(@NotNull Integer from, @NotNull Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
