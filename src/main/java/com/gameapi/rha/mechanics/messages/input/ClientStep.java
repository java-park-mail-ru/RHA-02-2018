package com.gameapi.rha.mechanics.messages.input;


import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ClientStep extends Message {


    private @NotNull List<Integer> from;

    private @NotNull List<Integer> to;


    public List<Integer> getFrom() {
        return from;
    }

    public void setFrom(List<Integer> from) {
        this.from = from;
    }

    public List<Integer> getTo() {
        return to;
    }

    public void setTo(List<Integer> to) {
        this.to = to;
    }




}
