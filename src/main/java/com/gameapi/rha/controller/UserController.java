package com.gameapi.rha.controller;

import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "bf-balance.herokuapp.com")
@RequestMapping("/users")
public class UserController {

    private static String SUCCESSFULLY_REGISTERED = "10";
    private static String     SUCCESSFULLY_AUTHED = "20";
    private static String SUCCESSFULLY_LOGGED_OUT = "30";
    private static String    SUCCESSFULLY_CHANGED = "40";
    private static String            ACCESS_ERROR = "50";
    private static String       WRONG_CREDENTIALS = "60";
    private static String     NOT_UNIQUE_USERNAME = "70";
    private static String   ALREADY_AUTHENTICATED = "80";
    private static String        UNEXPECTED_ERROR = "90";


    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user, HttpSession session) {

        // Аутентифицированный пользователь не может зарегистрироваться
        if (session.getAttribute("user") != null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ALREADY_AUTHENTICATED);

        if (UserService.create(user) != null) {
            sessionAuth(session, user);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFULLY_REGISTERED);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(NOT_UNIQUE_USERNAME);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity auth(@RequestBody User user, HttpSession session) {

        // Мы не можем дважды аутентицифироваться
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ALREADY_AUTHENTICATED);
        }

        // Если неверные учетные данные
        if (!UserService.check(user.getUsername(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(WRONG_CREDENTIALS);
        }

        sessionAuth(session, user);

        return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFULLY_AUTHED);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpSession session) {

        // Мы не можем выйти, не войдя
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ACCESS_ERROR);
        }

        session.setAttribute("user", null);
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFULLY_LOGGED_OUT);
    }

    @GetMapping(path = "/info")
    public ResponseEntity info(HttpSession session) {

        // Если пользователь не аутертифицирован, то у него нет доступа к информации о текущей сессии
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ACCESS_ERROR);
        }

        User result = UserService.userInfo((String) session.getAttribute("user"));
        if (result == null) {
            // Этого быть не может
            return ResponseEntity.status(HttpStatus.OK).body(UNEXPECTED_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@RequestBody User user, HttpSession session) throws Exception {

        // Без аутентификации нет доступа к изменению данных
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ACCESS_ERROR);
        }

        UserService.changeUser((String) session.getAttribute("user"), user);

        return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFULLY_CHANGED);
    }


    private static void sessionAuth(HttpSession session, User user)
    {
        session.setAttribute("user", user.getUsername());
        session.setMaxInactiveInterval(30*60);
    }
}


