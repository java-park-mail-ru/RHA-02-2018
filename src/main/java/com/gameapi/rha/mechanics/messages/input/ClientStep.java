package com.gameapi.rha.mechanics.messages.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gameapi.rha.websocket.Message;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class ClientStep extends Message {


    private @NotNull List<Integer> from;

    private @NotNull List<Integer> to;

//    @JsonCreator
//    public ClientStep(@JsonProperty("from") @NotNull List<Integer> from,@JsonProperty("to") @NotNull List<Integer> to) {
//        this.from = from;
//        this.to = to;
//    }

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
