package com.gameapi.rha.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@SuppressWarnings("PublicField")
public class Coords {

    public Coords(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public final int x;
    public final int y;

    @Override
    public String toString() {
        return '{'
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @SuppressWarnings("NewMethodNamingConvention")
    @NotNull
    public static Coords of(int x, int y) {
        return new Coords(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
