package com.gameapi.rha.controller;

import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import javax.websocket.Session;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private UserService someService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final HttpHeaders REQUEST_HEADERS = new HttpHeaders();

    private User user1=new User("Chapay","1yaKonArmiya",
            "General@RedArmy.ussr",1917);

    private  User user2=new User("Kolchak","BlackBaron",
            "Samoderzhec@WiteGuard.net",1905);

    private  User user3=new User("Nostradamus","",
            "IPredictThisWontWork@Astral.net",2012);

    private  User user4=new User("","somep",
            "ThisWontwork@Either.nope",null);

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void setHttpHeaders() {
        final List<String> origin = Collections.singletonList("http://localhost:5000");
        REQUEST_HEADERS.put(HttpHeaders.ORIGIN, origin);
        final List<String> contentType = Collections.singletonList("application/json");
        REQUEST_HEADERS.put(HttpHeaders.CONTENT_TYPE, contentType);
    }



    @Test
    @DisplayName("User creation")
    void create() {
        final HttpEntity<User> requestEntity = new HttpEntity<>(user1, REQUEST_HEADERS);

        final ResponseEntity<String> response = restTemplate.postForEntity("/users/create", requestEntity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Wrong user creation")
    void createW() {
    }

    @Test
    @DisplayName("User auth")
    void auth() {
    }

    @Test
    @DisplayName("Wrong user auth")
    void authW() {
    }

    @Test
    @DisplayName("User logout")
    void logout() {
    }

    @Test
    @DisplayName("Wrong user logout")
    void logoutW() {
    }

    @Test
    @DisplayName("Rating with pagination")
    void rating() {
    }

    @Test
    @DisplayName("Wrong rating with pagination")
    void ratingW() {
    }

    @Test
    @DisplayName("User info")
    void info() {
    }

    @Test
    @DisplayName("Wrong user info")
    void infoW() {
    }

    @Test
    @DisplayName("User change")
    void change() {
    }

    @Test
    @DisplayName("Wrong user change")
    void changeW() {
    }

    @Test
    @DisplayName("User password change")
    void changePass() {
    }

    @Test
    @DisplayName("Wrong user password change")
    void changePassW() {
    }
}