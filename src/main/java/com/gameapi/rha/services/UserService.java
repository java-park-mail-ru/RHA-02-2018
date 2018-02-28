package com.gameapi.rha.services;

import com.gameapi.rha.models.User;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {


    private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();

    public static User create(User user) {
        if (map.containsKey(user.getUsername()) )
            return null;
        map.put(user.getUsername(), user);
        return user;
    }

    public static Boolean exists(User user) {
        return map.containsKey(user.getUsername());
    }

    public static Boolean check (String username, String password) {
        return (map.containsKey(username) && map.get(username).checkPassword(password));
    }

    public static User userInfo(String username) {
        return null;
    }

    public static User changeUser(String prevUser, User newUser) throws Exception {

        User prev = map.get(prevUser);

        // Такого быть не должно
        if (prev == null) {
            return null;
        }

        prev.setEmail(newUser.getEmail());
        prev.setPassword(newUser.getPassword());
        prev.SaltHash();

        return prev;
    }
}
