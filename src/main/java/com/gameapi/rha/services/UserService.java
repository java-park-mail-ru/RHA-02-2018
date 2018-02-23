package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private static Map<String, User> map = new HashMap<String, User>();
    public static User create(String username, String password) throws Exception {
        User user = new User(username, password);

        if (map.containsKey(user.getUsername()))
            return new User("already exists", "00000000");
        map.put(user.getUsername(), user);
        System.out.println(getUserCount());
        return user;
    }

    public static Integer getUserCount(){
        return map.size();
    }
}
