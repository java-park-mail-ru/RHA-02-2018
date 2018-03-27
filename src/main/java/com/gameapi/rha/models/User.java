package com.gameapi.rha.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;




public class User {
  //@Nullable
  //  @Id
  //  @Column(name = "id",unique = true)
  //  @GeneratedValue(strategy = GenerationType.AUTO)
  //  @Column(name = "username",unique = true)
  private String username;
  //  @Column(name = "password")
  private String password;
  //  @Column(name = "email",unique = true)
  private String email;
  //  @Column(name = "rating")
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
      @JsonProperty("email") String email,
      @JsonProperty("rating") Integer rating
  ) {
    this.username = username;
    this.password = password;
    this.email = email;
    if(rating != null) {
      this.rating = rating;
     } else {
      this.rating = 0;
    }
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


