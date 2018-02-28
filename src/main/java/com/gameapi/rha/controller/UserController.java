package com.gameapi.rha.controller;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
//@SessionAttributes(value = "user")
@RequestMapping("/users")
public class UserController {

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getSession().getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        if (UserService.create(user) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }

    @PostMapping(path="/auth")
    public ResponseEntity auth(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Мы не можем дважды аутентицифироваться
        if (request.getSession().getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        // Если пароль неверный
        if(!UserService.check(user.getUsername(),user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        HttpSession session = request.getSession();
        if (UserService.sessionAuth(session, user)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        response.sendRedirect("/");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);

    }

    @PostMapping(path="/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Мы не можем выйти, не войдя
        if (request.getSession().getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null) {

            System.out.println(session.getId());
            session.setAttribute("user",null);
            session.invalidate();
            response.sendRedirect("/");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(session);
        }
    }

    @PostMapping(path="/info")
    public ResponseEntity info(HttpServletRequest request) {

        // Если пользователь не аутертифицирован, то у него нет доступа к информации о текущей сессии
        if (request.getSession().getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        User result = UserService.userInfo((String) request.getSession().getAttribute("user"));
        if (result == null) {
            // Этого быть не может
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Без аутентификации нет доступа к изменению данных
        if (request.getSession().getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        User result = UserService.changeUser((String) request.getSession().getAttribute("user"), user);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }
}


