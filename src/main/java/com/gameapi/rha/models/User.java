package com.gameapi.rha.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


import org.springframework.lang.Nullable;


public class User {
  @Nullable
  private String username;
  private String password;
  private String email;
  private Integer rating;

  /**
   * Default constructor for user.
   * @param username username
   * @param password password(Later Hashed and Salted)
   * @param email email
   */
  @JsonCreator
  public User(
      @JsonProperty("name") String username,
      @JsonProperty("password") String password,
      @JsonProperty("email") String email
  ) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.rating = 0;
  }

  public String getUsername() {
    return username;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rate) {
    rating = rate;
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


