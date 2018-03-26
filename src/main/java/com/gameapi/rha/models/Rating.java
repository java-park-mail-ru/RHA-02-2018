package com.gameapi.rha.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    private String username;
    private Integer score;

    @JsonCreator
    public Rating(
        @JsonProperty("username") String username,
        @JsonProperty("score") Integer score
    ) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
