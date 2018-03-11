package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {


    private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();
    public static Map<String,Integer> RatingTable = new HashMap<>();
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

    public static void ratingBuilder()
    {
        for(Map.Entry<String,User> user:map.entrySet())
        {
            RatingTable.put(user.getKey(),user.getValue().getRating());
        }
    };

    public static void changeUser(String prevUser, User newUser){

        final User prev = map.get(prevUser);

        // Такого быть не должно
        if (prev == null) {
            return;
        }
        map.get(prevUser).setEmail(newUser.getEmail());
        map.get(prevUser).setPassword(newUser.getPassword());
    }
}
