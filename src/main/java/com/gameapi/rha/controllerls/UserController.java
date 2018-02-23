package com.gameapi.rha.controllerls;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.gameapi.rha.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {


    @RequestMapping(value="/{username}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User findUser(@PathVariable  String username) throws Exception {
        System.out.println("It worked!!!");
        return new User("user", "password");
    }

}
