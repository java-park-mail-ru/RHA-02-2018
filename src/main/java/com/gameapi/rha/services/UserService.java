package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {


    private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();

    public static User putInMap(User user) {
        if (map.containsKey(user.getUsername()) )
            return null;
        map.put(user.getUsername(), user);
        return user;
    }

    public static Boolean check (String username, String password)  {
        return (map.containsKey(username) && map.get(username).checkPassword(password));
    }

    public static User userInfo(String username) {
        return map.get(username);
    }

    public static Map<String,Integer> ratingBuilder()
    {
        Map<String,Integer> ret = new HashMap<>();
        for(Map.Entry<String,User> user:map.entrySet())
        {
            ret.put(user.getKey(),user.getValue().getRating());
        }
        return ret;
    };

    public static void changeUser(String prevUser, User newUser){

        final User prev = map.get(prevUser);

        // Такого быть не должно
        if (prev == null) {
            return;
        }
        prev.setEmail(newUser.getEmail());
        prev.setPassword(newUser.getPassword());
    }
}
