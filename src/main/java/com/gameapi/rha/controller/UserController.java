package com.gameapi.rha.controller;


import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@CrossOrigin(origins = "http://bf-balance.herokuapp.com")
@RequestMapping("/users")
public class UserController {

    private enum UserStatus {
        SUCCESSFULLY_REGISTERED,
        SUCCESSFULLY_AUTHED,
        SUCCESSFULLY_LOGGED_OUT,
        SUCCESSFULLY_CHANGED,
        ACCESS_ERROR,
        WRONG_CREDENTIALS,
        NOT_UNIQUE_USERNAME,
        ALREADY_AUTHENTICATED,
        UNEXPECTED_ERROR
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user, HttpSession session) {

        // Аутентифицированный пользователь не может зарегистрироваться
        if (session.getAttribute("user") != null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ALREADY_AUTHENTICATED);


        if (UserService.putInMap(user) != null) {
            sessionAuth(session, user);
            return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_REGISTERED);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(UserStatus.NOT_UNIQUE_USERNAME);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity auth(@RequestBody User user, HttpSession session)throws NoSuchAlgorithmException,InvalidKeySpecException {

        // Мы не можем дважды аутентицифироваться
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ALREADY_AUTHENTICATED);
        }

        // Если неверные учетные данные
        if (!UserService.check(user.getUsername(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(UserStatus.WRONG_CREDENTIALS);
        }

        sessionAuth(session, user);

        return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_AUTHED);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpSession session) {

        // Мы не можем выйти, не войдя
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        session.setAttribute("user", null);
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_LOGGED_OUT);
    }

    @GetMapping(path = "/info")
    public ResponseEntity info(HttpSession session) {

        // Если пользователь не аутертифицирован, то у него нет доступа к информации о текущей сессии
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        final User result = UserService.userInfo((String) session.getAttribute("user"));
        if (result == null) {
            // Этого быть не может
            return ResponseEntity.status(HttpStatus.OK).body(UserStatus.UNEXPECTED_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@RequestBody User user, HttpSession session) {

        // Без аутентификации нет доступа к изменению данных
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        UserService.changeUser((String) session.getAttribute("user"), user);

        return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_CHANGED);
    }


    private static void sessionAuth(HttpSession session, User user)
    {
        session.setAttribute("user", user.getUsername());
        session.setMaxInactiveInterval(30*60);
    }
}



