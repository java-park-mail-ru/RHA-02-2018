package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import com.gameapi.rha.websocket.RemotePointService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class ClientTurnService {

    @org.jetbrains.annotations.NotNull
    private final RemotePointService remotePointService;

    public ClientTurnService(@org.jetbrains.annotations.NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void turn(){

    }
}
