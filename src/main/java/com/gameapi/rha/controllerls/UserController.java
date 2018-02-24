package com.gameapi.rha.controllerls;


import com.gameapi.rha.models.User;
import com.gameapi.rha.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping(path = "/create/{name}")
    public ResponseEntity create(@RequestBody User user) throws Exception {

        if (UserService.create(user) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }

    // это лишь только для проверки, честно, я не буду передавать данные о пользователе через GET
    @GetMapping(
            path = "/create/{username}/{email}/{password}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    ) public ResponseEntity create(
            @PathVariable(name = "username") String username,
            @PathVariable(name = "email") String email,
            @PathVariable(name = "password") String password
    ) throws Exception {
        if (UserService.create(username, email, password) != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(new User(username, email, password));
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
}


