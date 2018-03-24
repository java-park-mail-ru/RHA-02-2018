package com.gameapi.rha.services;

import com.gameapi.rha.models.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {


    private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();
    public static Map<String,Integer> RatingTable = new HashMap<>();
    public static User putInMap(User user) {
        if (map.containsKey(user.getEmail()) )
            return null;
        map.put(user.getEmail(), user);
        return user;
    }


    public static Map<String,Integer> Rating(Integer page){
        Iterator itr = map.entrySet().iterator();
        if (page==null)
        {
            page=0;
        }
        Integer elements=2;
        while (page-- > 0 && itr.hasNext()) {
            itr.next();
        }
        Map<String,Integer> result=new HashMap<>();

     while(elements-- > 0 && itr.hasNext())
        {
            Map.Entry<String,User> entry = (Map.Entry<String, User>) itr.next();
            result.put(entry.getValue().getUsername(), entry.getValue().getRating());
        }
        return result;
    }


    public static Boolean check (String email, String password)  {
        return (map.containsKey(email) && map.get(email).checkPassword(password));
    }

    public static User userInfo(String email) {
        return map.get(email);
    }

    public static void ratingBuilder()
    {
        for(Map.Entry<String,User> user:map.entrySet())
        {
            RatingTable.put(user.getValue().getUsername(),user.getValue().getRating());
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
