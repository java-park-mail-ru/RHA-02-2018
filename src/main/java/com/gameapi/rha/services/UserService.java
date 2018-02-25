package com.gameapi.rha.services;

import com.gameapi.rha.models.User;
//import sun.security.util.Password;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static Map<String, User> map = new HashMap<String, User>();

    public static User create(User user) throws Exception {
        user.SaltHash();
        if (map.containsKey(user.getUsername()) )
            return null;
        map.put(user.getUsername(), user);
        return user;
    }

    public static User create(String username, String email, String password) throws Exception {
        if (map.containsKey(username) )
            return null;
        User user = new User(username, email, password);
        map.put(username, user);
        return user;
    }


    public static Boolean exists(User user) {
        return map.containsKey(user.getUsername());
    }
    public static Boolean exists(String user) {
        return map.containsKey(user);
    }

    public static Boolean auth(String user,String pass) throws Exception {
        return (map.containsKey(user) && map.get(user).checkPassword(pass));
    }

    private static Integer getUserCount() {
        return map.size();
    }
}
