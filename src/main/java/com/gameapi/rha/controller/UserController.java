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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user) throws Exception {

        System.out.println("________________");

        if (UserService.create(user) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }
//
    @PostMapping(path="/auth")
    public ResponseEntity auth(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) throws Exception {

        if(UserService.check(user.getUsername(),user.getPassword())){
            HttpSession session = request.getSession();
            if (UserService.sessionAuth(session, user)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
            }
            response.sendRedirect("/");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }


    @PostMapping(path="/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null) {

            System.out.println(session.getId());
            session.setAttribute("user",null);
            //setting session to expiry in 30 mins
            session.invalidate();
            response.sendRedirect("/");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(session);
        }

    }

    @PostMapping(path="/info")
    public ResponseEntity info(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        User result = UserService.userInfo(username);
        if (result == null) {
            // Этого быть не может
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}


