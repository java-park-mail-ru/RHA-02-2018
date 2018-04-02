package com.gameapi.rha.services;

import static org.junit.jupiter.api.Assertions.*;
import com.gameapi.rha.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

class UserServiceTest {
    private  User user;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        System.out.println("Test for UserService started.");
        user=new User("Chapay","1yaKonArmiya",
                "General@RedArmy.ussr",1917);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        user=null;
        System.out.println("Test for UserService Finished.");
    }





//    @Test(expected = NullPointerException.class)
//    void createNullUser() {
//        UserService.createUser( null );
//    }
//
//    @Test(expected = NullPointerException.class)
//    void createShittyUser() {
//        UserService.createUser( new User("user","pass",null,null) );
//    }
//
//    @Test
//    void createSimpleUser() {
//        UserService.createUser( user );
//    }
//
//    @Test(expexted = DuplicateKeyException.class)
//    void createDuplicateUser() {
//        UserService.createUser( user );
//        UserService.createUser( user );
//    }
//
//
//    @Test(expected = NullPointerException.class)
//    void getNoRating() {
//        UserService.createUser( new User("user","pass",null,null) );
//    }


    @org.junit.jupiter.api.Test
    void check() {
    }

    @org.junit.jupiter.api.Test
    void userInfo() {
    }

    @org.junit.jupiter.api.Test
    void changeUser() {
    }
}