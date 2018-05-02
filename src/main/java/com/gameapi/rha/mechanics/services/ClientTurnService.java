package com.gameapi.rha.mechanics.services;

import com.gameapi.rha.mechanics.messages.input.ClientTurn;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class ClientTurnService {
    private final Map<String, List<ClientTurn>> turns = new HashMap<>();

    public void pushClientSnap(@NotNull String user, @NotNull ClientTurn turn) {
        this.turns.putIfAbsent(user, new ArrayList<>());
        final List<ClientTurn> clientSnaps = turns.get(user);
        clientSnaps.add(turn);
    }

    @NotNull
    public List<ClientTurn> getSnapForUser(@NotNull String user) {
        return turns.getOrDefault(user, Collections.emptyList());
    }
    public void clearForUser(String username) {
    }
}
