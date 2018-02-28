package com.gameapi.rha.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.*;




public class User {
    private String username;
    private String password;
    private String email;
    private UUID uID;

    @JsonCreator
    public User(
            @JsonProperty("name") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email
    ) throws Exception {
        this.username = username;
        this.password = Password.getSaltedHash(password);
        this.uID = UUID.randomUUID();
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void saltHash() throws Exception{
        this.password = Password.getSaltedHash(this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getuID() {
        return uID;
    }

    public void setPassword(String password) throws Exception {
        this.password = Password.getSaltedHash(password);
    }

    public Boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


