package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {


    private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();

    public static User putInMap(User user) {
        if (map.containsKey(user.getUsername()) )
            return null;
        map.put(user.getUsername(), user);
        return user;
    }
//
//    public static Boolean exists(User user) {
//        return map.containsKey(user.getUsername());
//    }

    public static Boolean check (String username, String password) throws NoSuchAlgorithmException,InvalidKeySpecException {
        return (map.containsKey(username) && map.get(username).checkPassword(password));
    }

    public static User userInfo(String username) {
        return map.get(username);
    }

    public static void changeUser(String prevUser, User newUser) throws NoSuchAlgorithmException,InvalidKeySpecException {

        final User prev = map.get(prevUser);

        // Такого быть не должно
        if (prev == null) {
            return;
        }

        prev.setEmail(newUser.getEmail());
        prev.setPassword(newUser.getPassword());
        prev.saltHash();

//        return prev;
    }
}
