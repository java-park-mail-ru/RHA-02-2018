package com.gameapi.rha.controller;

import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

//    @MockBean
//    private JdbcTemplate jdbc;

    @MockBean(name="userService")
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final HttpHeaders REQUEST_HEADERS = new HttpHeaders();

    private User user1=new User("Chapay","1yaKonArmiya",
            "General@RedArmy.ussr",1917);

    private  User user2=new User("Kolchak","BlackBaron",
            "Samoderzhec@WiteGuard.net",1905);

    private  User user3=new User("Nostradamus",null,
            "IPredictThisWontWork@Astral.net",2012);

    private  User user4=new User(null,"somep",
            "ThisWontwork@Either.nope",null);

    UserControllerTest() {

        final List<String> origin = Collections.singletonList("http://localhost:5000");
        REQUEST_HEADERS.put(HttpHeaders.ORIGIN, origin);
        final List<String> contentType = Collections.singletonList("application/json");
        REQUEST_HEADERS.put(HttpHeaders.CONTENT_TYPE, contentType);
    }

    @Test
    @DisplayName("User creation")
    void create() {
        when(userService.createUser(any(User.class))).thenReturn(user1);
//        Mockito.when(userService.createUser(user1)).thenReturn(user1);
        final HttpEntity<User> requestEntity = new HttpEntity<>(user1, REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/create", requestEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("User with no password creation")
    void createP() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user3, REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/create", requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("User with no username creation")
    void createW() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user4, REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/create", requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }


    @Test
    @DisplayName("User auth")
    void auth() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user1, REQUEST_HEADERS);
        when(userService.check(anyString(),anyString())).thenReturn(user1);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/auth", requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    @DisplayName("Wrong user auth")
    void authW() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user3, REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/create", requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }


    @Test
    @DisplayName("Wrong user logout")
    void logoutW() {
        final HttpEntity requestEntity = new HttpEntity<>(REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/logout", requestEntity, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    @DisplayName("Rating unlogged")
    void ratingW() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
                "/users/rating/1",String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    @DisplayName("User info unlogged")
    void info() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
                "/users/info",String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }


    @Test
    @DisplayName("User change unlogged")
    void change() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user3, REQUEST_HEADERS);

        final ResponseEntity<String> response = restTemplate.postForEntity(
                "/users/change",requestEntity,String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }


    @Test
    @DisplayName("User password change")
    void changePass() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user3, REQUEST_HEADERS);
        final ResponseEntity<String> response = restTemplate.postForEntity("/users/chpwd", requestEntity, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());



    }


}