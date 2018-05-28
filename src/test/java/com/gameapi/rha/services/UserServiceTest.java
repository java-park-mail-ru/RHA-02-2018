package com.gameapi.rha.services;

import static org.junit.jupiter.api.Assertions.*;

import com.gameapi.rha.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService someService;

    @Autowired
    private JdbcTemplate jdbc;

    private  User user1=new User("Chapay","1yaKonArmiya",
            "General@RedArmy.ussr",1917);

    private  User user2=new User("Kolchak","BlackBaron",
            "Samoderzhec@WiteGuard.net",1905);

    private  User user3=new User("Nostradamus","",
                                        "IPredictThisWontWork@Astral.net",2012);

    private  User user4=new User("","somep",
            "ThisWontwork@Either.nope",null);


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        System.out.println("Test for UserService started.");}

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        user1=null;
        System.out.println("Test for UserService Finished.");
    }

    @DisplayName("Simple user creation")
    @Test
    void createSimpleUser() {
        final User createdUser = someService.createUser(user1);
        assertEquals(createdUser.getUsername(),user1.getUsername());
        assertEquals(createdUser.getEmail(),user1.getEmail());
        assertEquals(createdUser.getPassword(),user1.getPassword());
        assertEquals(createdUser.getRating(),user1.getRating());


    }



    @DisplayName("Null user creation")
    @Test
    void createNullUser() {
        assertThrows( NullPointerException.class,()->{ someService.createUser( null );});
    }


    @DisplayName("Wrong user creations")
    @Test
    void createShittyUser() {
        assertThrows( DataAccessException.class,
                ()->{someService.createUser( new User("user","pass",null,null) );});
        assertThrows( DataAccessException.class,
                ()->{someService.createUser( user3 );});
        assertThrows( DataAccessException.class,
                ()->{someService.createUser( user4 );});

    }



    @DisplayName("Same user creation")
    @Test
    void createDuplicateUser() {
        User us2=someService.createUser( user2 );
        assertThrows( DuplicateKeyException.class,
                ()->{

                    someService.createUser( user2 );
        });
    }

    @DisplayName("Simple rating")
    @Test
    void getNormRating() {
        someService.createUser(user1);
        someService.createUser( user2 );
//        List<Object> lst=someService.rating(0,user2.getUsername());
//        assertEquals(user1.getRating(),lst.get(0).get(user1.getUsername()));
    }

    @DisplayName("Wrong page rating")
    @Test
    void getNoRating() {
        assertThrows(DataIntegrityViolationException.class,
                ()->{
                    someService.createUser( user2 );
                    someService.rating(-5,user2.getUsername());
        });
    }

    @Test
    @DisplayName("Simple user check")
    void Rightcheck() {
        User user=new User("Kolchak","BlackBaron",
                "Samoderzhec@WiteGuard.net",1905);
        user.saltHash();
        someService.createUser( user );
        assertEquals(user.getUsername(),someService.check(user.getEmail(),"BlackBaron").getUsername());

    }

    @Test
    @DisplayName("Wrong user check")
    void Wrongcheck() {
        assertThrows(EmptyResultDataAccessException.class,()->{someService.check(user1.getEmail(),user1.getPassword());});
        someService.createUser( user1 );
    }

    @Test
    @DisplayName("Simple user info")
    void userInfo() {
        someService.createUser( user1 );
        assertEquals(user1.getEmail(),someService.userInfo(user1.getUsername()).getEmail());
    }

    @Test
    @DisplayName("Wrong user info")
    void userInfoW() {
        assertThrows(EmptyResultDataAccessException.class,()->{
            someService.userInfo(user1.getUsername());
        });
    }

    @Test
    @DisplayName("Simple user changes")
    void changeUser() {
        someService.createUser( user1 );
        user1.setPassword("Revolution has no END!!!");
        someService.changeUser( user1 );
        assertEquals(user1.getPassword(),someService.userInfo(user1.getUsername()).getPassword());

    }

}