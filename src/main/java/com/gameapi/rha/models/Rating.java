package com.gameapi.rha.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    private String email;
    private Integer score;

    @JsonCreator
    public Rating(
        @JsonProperty("email") String email,
        @JsonProperty("score") Integer score
    ) {
        this.email = email;
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
