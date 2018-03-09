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
    ) {
        this.username = username;
        this.password = password;
        this.uID = UUID.randomUUID();
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public UUID getuID() {
        return uID;
    }

    public void setPassword(String password) {
        this.password = Password.getSaltedHash(password);
    }

    public Boolean checkPassword(String pass) {
        return Password.check(pass, this.password);
    }

    public void saltHash() {
        this.password = Password.getSaltedHash(this.password);
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


