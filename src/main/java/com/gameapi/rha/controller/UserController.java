package com.gameapi.rha.controller;

//
//import com.gameapi.rha.servlets.AuthServlet.*;
import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.apache.catalina.connector.Request;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Dictionary;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user) throws Exception {

        if (UserService.create(user) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }
//
    @PostMapping(path="/auth")
    public ResponseEntity auth(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) throws Exception {

        if(UserService.auth(user.getUsername(),user.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("user", "Pankaj");
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30*60);
            Cookie userName = new Cookie("Auth",user.getUsername());
            userName.setMaxAge(30*60);
            System.out.println("<font color=red>All right.</font>");
            response.addCookie(userName);
            response.sendRedirect("/");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(user);
        }

    }

    // это лишь только для проверки, честно, я не буду передавать данные о пользователе через GET
//    @GetMapping(
//            path = "/create/{username}/{email}/{password}",
//            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    ) public ResponseEntity create(
//            @PathVariable(name = "username") String username,
//            @PathVariable(name = "email") String email,
//            @PathVariable(name = "password") String password
//    ) throws Exception {
//        if (UserService.create(username, email, password) != null)
//            return ResponseEntity.status(HttpStatus.CREATED).body(new User(username, email, password));
//        else
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//    }


}


