package com.gameapi.rha.models;


import java.util.UUID;
import com.fasterxml.jackson.annotation.*;
//import org.springframework.lang.Nullable;


public class User {
//    @Nullable
    private String username;
    private String password;
    private String email;
    private Integer rating;
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
        this.rating=0;
    }

    public String getUsername() {
        return username;
    }

    public Integer getRating() { return rating;}

    public void setRating(Integer rate) { rating=rate;}

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


